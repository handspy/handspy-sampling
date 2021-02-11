package pt.up.hs.sampling.processing.utils;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import java.util.*;

public class JobParameterUtils {

    public static JobParameters mapToJobParameters(Map<Long, Long> map, String prefix) {
        Map<String, JobParameter> params = new HashMap<>();
        for (Long key: map.keySet()) {
            params.put(prefix + key, new JobParameter(map.get(key)));
        }
        return new JobParameters(params);
    }

    public static Map<Long, Long> jobParametersToMap(Map<String, Object> jobParameters, String prefix) {
        Map<Long, Long> params = new HashMap<>();
        jobParameters.keySet().parallelStream()
            .forEach(jobParamKey -> {
                if (jobParamKey.startsWith(prefix)) {
                    params.put(
                        Long.parseLong(jobParamKey.substring(prefix.length())),
                        (Long) jobParameters.get(jobParamKey)
                    );
                }
            });
        return params;
    }

    public static JobParameters longListToJobParameters(List<Long> els, String prefix) {
        Map<String, JobParameter> params = new HashMap<>();
        params.put(prefix + "size", new JobParameter((long) els.size()));
        for (int i = 0; i < els.size(); i++) {
            params.put(prefix + i, new JobParameter(els.get(i)));
        }
        return new JobParameters(params);
    }

    public static List<Long> jobParametersToLongList(Map<String, Object> jobParameters, String prefix) {
        List<Long> params = new ArrayList<>();
        int size = ((Long) jobParameters.get(prefix + "size")).intValue();
        for (int i = 0; i < size; i++) {
            params.add((Long) jobParameters.get(prefix + i));
        }
        return params;
    }
}
