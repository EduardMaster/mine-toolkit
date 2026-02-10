package br.com.eduard.storage.storables;

import org.bukkit.util.Vector;

import br.com.eduard.storage.Storable;

public class VectorStorable implements Storable<Vector> {
    @Override
    public Vector newInstance() {
        return new Vector();
    }
}
