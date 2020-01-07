package com.base.net.websocket;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import com.base.component.GlobalConfigComponent;
import com.base.config.WebServerConfig;
import com.base.net.AbstractNettyComponent;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * WebSocket服务器组件
 * 
 * @author dream
 *
 */
public class NettyWebSocketComponent extends AbstractNettyComponent
{
    // public class GameServerClientHandler extends AbstractClientPacketHandler
    // {
    // @Override
    // public AbstractCommandComponent getComponent()
    // {
    // UserCommandComponent cm = ComponentManager.getInstance().getComponent(UserCommandComponent.class);
    // return cm;
    // }
    // }

    @Override
    public void initGroup()
    {
        int count = Runtime.getRuntime().availableProcessors();
        parentCount = count * 1;
        childCount = count * 3 + 1;
    }

    @Override
    protected void acceptorInit(ChannelPipeline pipeline)
    {
        pipeline.addLast("http-codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());

        WebServerConfig web = GlobalConfigComponent.getConfig().web;
        // web socket 连接握手，https处理
        if (web.https.isOpen)
        {
            try
            {
                // 加载jks文件，password对应
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(new FileInputStream(web.https.jks), web.https.password.toCharArray());

                // KeyManagerFactory充当基于密钥内容源的密钥管理器的工厂
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(keyStore, web.https.password.toCharArray());

                // SSLContext的实例表示安全套接字协议的实现，它充当用于安全套接字工厂或 SSLEngine的工厂
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(kmf.getKeyManagers(), null, null);
                SSLEngine engine = sslContext.createSSLEngine();
                engine.setUseClientMode(false);

                pipeline.addLast(new SslHandler(engine));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        pipeline.addLast("http-codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());

        // pipeline.addLast("handler", new WebSocketClientIOHandler(new GameServerClientHandler()));
    }

    @Override
    protected int getPort()
    {
        return Integer.valueOf(GlobalConfigComponent.getConfig().server.ports.split(",")[0]);
    }
}
