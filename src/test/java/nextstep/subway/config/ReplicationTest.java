package nextstep.subway.config;

import static nextstep.subway.common.ReplicationRoutingDataSource.DATASOURCE_KEY_MASTER;
import static nextstep.subway.common.ReplicationRoutingDataSource.DATASOURCE_KEY_SLAVE;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import nextstep.subway.common.ReplicationRoutingDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ReplicationTest {

    private static final String Test_Method_Name = "determineCurrentLookupKey";

    @Test
    @DisplayName("쓰기 전용 트랜잭션 테스트")
    @Transactional(readOnly = false)
    void writeOnlyTransactionTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();

        // when
        Method determineCurrentLookupKey = ReplicationRoutingDataSource.class.getDeclaredMethod(Test_Method_Name);
        determineCurrentLookupKey.setAccessible(true);

        String dataSourceType = (String) determineCurrentLookupKey.invoke(replicationRoutingDataSource);

        // then
        assertThat(dataSourceType).isEqualTo(DATASOURCE_KEY_MASTER);
    }

    @Test
    @DisplayName("읽기 전용 트랜잭션 테스트")
    @Transactional(readOnly = true)
    void readOnlyTransactionTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();

        // when
        Method determineCurrentLookupKey = ReplicationRoutingDataSource.class.getDeclaredMethod(Test_Method_Name);
        determineCurrentLookupKey.setAccessible(true);

        String dataSourceType = (String) determineCurrentLookupKey.invoke(replicationRoutingDataSource);

        // then
        assertThat(dataSourceType).isEqualTo(DATASOURCE_KEY_SLAVE);
    }
}
