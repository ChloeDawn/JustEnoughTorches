package net.insomniakitten.jetorches.util;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.LinkedList;
import java.util.List;

public final class RegistryHolder<V extends IForgeRegistryEntry<V>> {

    private final List<V> entries = new LinkedList<>();

    public Registry begin(RegistryEvent.Register<V> event) {
        return new Registry(event, entries);
    }

    public ImmutableList<V> entries() {
        return ImmutableList.copyOf(entries);
    }

    public final class Registry {
        private final RegistryEvent.Register<V> event;
        private final List<V> entries;

        private Registry(RegistryEvent.Register<V> event, List<V> entries) {
            this.event = event;
            this.entries = entries;
        }

        public final Registry register(V entry) {
            if (entry.getRegistryName() != null) {
                if (!entries.contains(entry)) {
                    event.getRegistry().register(entry);
                    entries.add(entry);
                }
            }
            return this;
        }

        @SafeVarargs
        public final Registry registerAll(V... entries) {
            for (V entry : entries) {
                register(entry);
            }
            return this;
        }
    }

}
