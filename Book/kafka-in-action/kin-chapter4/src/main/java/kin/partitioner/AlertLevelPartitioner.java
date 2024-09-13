package kin.partitioner;

import kin.model.Alert;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class AlertLevelPartitioner implements Partitioner {
    @Override
    public int partition(final String topic,
                         final Object objectKey,
                         final byte[] keyBytes,
                         final Object value,
                         final byte[] valueBytes,
                         final Cluster cluster) {

        int criticalLevelPartition = findCriticalPartitionNumber(cluster, topic);

        return isCriticalLevel(((Alert) objectKey).getAlertLevel()) ?
                criticalLevelPartition :
                findRandomPartition(cluster, topic, objectKey);
    }

    public int findCriticalPartitionNumber(Cluster cluster, String topic) {
        //not using parameters but could if needed for your logic
        return 0;
    }

    public int findRandomPartition(Cluster cluster, String topic, Object objectKey) {
        //not using parameter objectKey but could if needed for your logic
        List<PartitionInfo> partitionMetaList =
                cluster.availablePartitionsForTopic(topic);

        Random randomPart = new Random();
        return randomPart.nextInt(partitionMetaList.size());
    }

    public boolean isCriticalLevel(String level) {
        if (level.toUpperCase().contains("CRITICAL")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
