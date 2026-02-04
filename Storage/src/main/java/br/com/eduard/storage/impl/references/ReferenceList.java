package br.com.eduard.storage.impl.references;

import br.com.eduard.storage.StorageAPI;
import br.com.eduard.storage.api.ReferenceBase;
import br.com.eduard.storage.api.StorageInfo;

import java.util.List;

public class ReferenceList extends ReferenceBase<List<Object>> {
	private final List<Object> realList;
	public ReferenceList(StorageInfo info, List<Object> references, List<Object> realList) {
		super(info, null);
		setRestore(references);
		this.realList = realList;
	}

	@Override
	public void update() {
		for (Object key : getRestore()) {
			realList.add(StorageAPI.getObjectByKey(getInfo().getType(), key ));
		}
	}



}
