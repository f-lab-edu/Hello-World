package me.soo.helloworld.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static me.soo.helloworld.util.constant.DataSourceTypes.*;

@Configuration
@EnableTransactionManagement
@Profile("prod")
public class DataSourceConfig {

    /*
        외부(properties) 파일에서 주입 받은 값을 이용해 마스터와 슬레이브 각각의 DataSource 를 설정합니다.
     */
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    /*
        Master-Slave 구조의 Replication 은 슬레이브 DB가 마스터 DB의 바이너리 로그 파일을 읽어온 후 이것을 바탕으로 데이터를 동기화 시키는
        방식으로 이루어집니다.

        마스터의 부하 분산을 위해 읽기 작업은 주로 슬레이브에서 이루어지는데 간혹 마스터-슬레이브 간의 시차 때문에 슬레이브에 업데이트가 제대로
        반영이 안될 수 있습니다. 때문에 데이터 정합성이 중요한 읽기 작업은 읽기라도 마스터에서 읽어야할 경우가 있으며, 이를 위해 마스터/슬레이브의 분기를
        @Transactional(readOnly = true)를 기준으로 동적으로 나누어주는 역할을 하는 것이 바로 ReplicationRoutingDataSource 클래스 입니다.

        해당 클래스는 추상 클래스인 AbstractRoutingDataSource 를 상속받아 추상 메소드인 determineCurrentLookupKey 를 구현했으며 readOnly
        트랜잭션이 활성화되어 있는 경우는 slave 아닌 경우는 master 를 리턴하고, 이를 키 값으로 활용해 그에 맞는 dataSource value 를 가져옵니다.

        기본 값은 masterDataSource 로 설정되어 readOnly 어노테이션이 없는 경우는 자동으로 마스터 DB를 사용하게 됩니다.
     */
    @Bean(name = "routingDataSource")
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("slaveDataSource") DataSource slaveDatasource) {
        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(MASTER_DB, masterDataSource);
        dataSourceMap.put(SLAVE_DB, slaveDatasource);
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    /*
        트랜잭션이 걸릴 경우 Spring 은 무조건적으로 Connection 객체를 확보하는 경향이 있습니다.
        이러한 단점을 보완하기 위해서 LazyConnection Proxy 로 DataSource 를 감싸주면 실제로 쿼리가 발생할 때만 커넥션을 호출합니다.

        또한, LazyConnection 이 필수적으로 활용되어야 하는 이유는 동기화 시점과 RoutingDataSource 가 Connection 객체를 가져오는 시점과
        관련이 있습니다.

        @Transactional 의 기본 동작 순서는 'TransactionManager 선별 -> DataSource 에서 Connection 획득 -> Transaction 동기화' 로
        트랜잭션 동기화 전에 DataSource 에서 Connection 을 확보하므로 ReplicationDataSource 가 제대로 작동하지 못합니다.

        이때 LazyConnection Proxy 을 활용해주는 경우 순서가
        'TransactionManager 선별 -> LazyConnectionDataSourceProxy 의 Connection Proxy 객체 획득 -> Transaction 동기화(Synchronization)
        -> 실제 쿼리 호출시 ReplicationRoutingDataSource.getConnection() 및 determineCurrentLookupKey() 호출' 로 바뀌게 되므로
        위의 RoutingDataSource 가 제대로 동작할 수 있게 됩니다.

        또한, 트랜잭션 매니저 혹은 DB 프레임워크가 proxyDataSource 에만 의존하도록 추상화 해줌으로써 각각의 DataSource 에 대한 직접 의존관계가
        사라져 변화에는 닫혀있고, 확장에는 열려 있는 OCP 구조로 DataSource 를 만들어 유지보수성도 높일 수 있습니다.
     */
    @Bean(name = "proxyDataSource")
    public DataSource proxyDataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("proxyDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
