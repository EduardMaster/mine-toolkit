package br.com.eduard.storage.impl.references;


import br.com.eduard.storage.StorageAPI;
import br.com.eduard.storage.api.ReferenceBase;
import br.com.eduard.storage.api.StorageInfo;

public class ReferenceValue extends ReferenceBase<Object> {

    public ReferenceValue(StorageInfo info, Object instance, Object key) {
        super(info, instance);
        setRestore(key);
    }

    public void update() {
        try {
            Object reference = StorageAPI.getObjectByKey(getInfo().getType() , getRestore());
            getInfo().getField().set(getInstance(), reference);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
