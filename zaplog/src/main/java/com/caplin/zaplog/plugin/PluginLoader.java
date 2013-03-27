package com.caplin.zaplog.plugin;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class PluginLoader
{
    private static PluginLoader pluginLoader;
    private ServiceLoader<ZapPlugin> serviceLoader;
    private Logger logger = Logger.getLogger(getClass().getName());
 
    private PluginLoader()
    {
        serviceLoader = ServiceLoader.load(ZapPlugin.class);
    }
 
    public static PluginLoader getInstance()
    {
        if(pluginLoader == null)
        {
            pluginLoader = new PluginLoader();
        }
        return pluginLoader;
    }
 
    public Iterator<ZapPlugin> getPlugins()
    {
        return serviceLoader.iterator();
    }
 
    public void initPlugins()
    {
        Iterator<ZapPlugin> iterator = getPlugins();
        if(!iterator.hasNext())
        {
            logger.info("No plugins were found!");
        }
        
        while(iterator.hasNext())
        {
            ZapPlugin plugin = iterator.next();
            logger.info("Initialising the plugin " + plugin.getName());
            plugin.init();
        }
    }
}