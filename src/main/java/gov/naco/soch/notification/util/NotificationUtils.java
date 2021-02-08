package gov.naco.soch.notification.util;

import java.util.ArrayList;
import java.util.List;

public class NotificationUtils {
	
	public static <T> List<List<T>> getBatches(List<T> collection, int batchSize) {
	    List<List<T>> batches = new ArrayList<>();
	    for (int i = 0; i < collection.size(); i += batchSize) {
	        batches.add(collection.subList(i, Math.min(i + batchSize, collection.size())));
	    }
	    return batches;
	}

}
