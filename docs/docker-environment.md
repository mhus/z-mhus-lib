
# Set mhus-config 

In system section add:

```
<initiator class="de.mhus.lib.core.system.DockerInitializer" />
```

Additional section:

```
<de.mhus.lib.core.service.ServerIdent
    persistent="no"
    pattern="$#hostname$-$containerShortId$"
/>
```
