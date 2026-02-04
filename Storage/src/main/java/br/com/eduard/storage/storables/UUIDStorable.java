package br.com.eduard.storage.storables;

import br.com.eduard.storage.api.Storable;
import br.com.eduard.storage.api.annotations.StorageAttributes;

import java.util.UUID;


@StorageAttributes(inline = true)
public class UUIDStorable implements Storable<UUID> {

	public UUID restore(String object) {
		try {
			return UUID.fromString(object);
		} catch (Exception e) {
			return null;
		}
	}

	public String store(UUID object) {
		return object.toString();
	}

}
