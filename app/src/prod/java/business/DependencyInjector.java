package business;

import business.service.ServiceInjector;
import business.storage.StorageInjector;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class DependencyInjector {
    private ServiceInjector serviceInjector;
    private StorageInjector storageInjector;

    public DependencyInjector(ServiceInjector serviceInjector, StorageInjector storageInjector) {
        this.serviceInjector = serviceInjector;
        this.storageInjector = storageInjector;
    }

    public OAuthBusiness getOAuthBusiness() {
        return new OAuthBusinessImpl(serviceInjector.getLoginSvcInterface(), storageInjector.getPreferenceStorage());
    }
}
