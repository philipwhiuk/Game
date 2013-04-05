package com.whiuk.philip.game.server.data;

import org.springframework.stereotype.Service;

import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Versioned;

/**
 * Voldemort implementation of data service.
 * @author Philip Whitehouse
 *
 * @param <K>
 * @param <V>
 */
@Service
public class VoldemortDataService<K, V> implements DataService<K, V> {

    /**
     * Address.
     */
    public static final String VOLDEMORT_ADDRESS = "localhost:6666";
    /**
     * Store name.
     */
    public static final String VOLDEMORT_STORE_NAME = "my_store_name";

    /**
     * Client object.
     */
    private StoreClient<K, V> client;

    /**
     *
     */
    public VoldemortDataService() {
        String bootstrapUrl = "tcp://" + VOLDEMORT_ADDRESS;
        StoreClientFactory factory = new SocketStoreClientFactory(
            new ClientConfig().setBootstrapUrls(bootstrapUrl));
        client = factory.getStoreClient(VOLDEMORT_STORE_NAME);
    }

    @Override
    public final V get(final K key) {
		Versioned<V> value = client.get(key);
		return value.getValue();
	}

	@Override
	public final void put(final K key, final V value) {
		 client.put(key, value);
	}

}
