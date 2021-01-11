package net.thumbtack.school.asynch.rest;


import io.netty.bootstrap.Bootstrap;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class RestService {

    private final static String REQUEST = "GET /api/debug/settings HTTP/1.1\r\n" +
            "Host: localhost\r\n" +
            "Accept: */*\r\n" +
            "Connection: close\r\n\r\n";


    public void process(ChannelHandlerContext ctx) throws Exception {
        Channel inboundChannel = ctx.channel();
        Bootstrap b = new Bootstrap();
        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("localhost", 8080))
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    public void initChannel(Channel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new HttpResponseDecoder());
                        p.addLast(new HttpObjectAggregator(100 * 1024));
                        p.addLast(new FirstClientHandler(ctx));
                    }
                });
        b.connect();
    }


    private static class FirstClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

        private final ChannelHandlerContext serverctx;

        public FirstClientHandler(ChannelHandlerContext serverctx) {
            this.serverctx = serverctx;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(REQUEST, CharsetUtil.UTF_8));
        }


        @Override
        public void channelRead0(ChannelHandlerContext ctx, FullHttpResponse in) {
            String result = in.content().toString(CharsetUtil.UTF_8);
            process(serverctx, result);
        }

        public void process(ChannelHandlerContext ctx, String result){
            Channel inboundChannel = ctx.channel();
            Bootstrap b = new Bootstrap();
            b.group(inboundChannel.eventLoop())
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", 8080))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        public void initChannel(Channel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpResponseDecoder());
                            p.addLast(new HttpObjectAggregator(100 * 1024));
                            p.addLast(new SecondClientHandler(ctx, result));
                        }
                    });
            b.connect();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }

    private static class SecondClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

        private final ChannelHandlerContext serverctx;
        private final String firstResult;

        public SecondClientHandler(ChannelHandlerContext serverctx, String firstResult) {
            this.serverctx = serverctx;
            this.firstResult = firstResult;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            FullHttpRequest request = new DefaultFullHttpRequest(
                    HTTP_1_1,
                    HttpMethod.POST,
                    "/api/accounts/",
                    Unpooled.copiedBuffer("{\"firstName\":\"firstName\",\"lastName\":\"lastName\",\"patronymic\":\"patronymic\",\"login\":\"loginloginlogin3\",\"password\":\"password99999\"}", CharsetUtil.UTF_8));
            request.headers().set(CONTENT_TYPE, "application/json");
            request.headers().set(CONTENT_LENGTH, request.content().readableBytes());
            request.headers().set(CONNECTION, "close");
            request.headers().set(HOST, "localhost");
            ctx.writeAndFlush(request);
        }


        @Override
        public void channelRead0(ChannelHandlerContext ctx, FullHttpResponse in) {
            String secondResult = in.content().toString(CharsetUtil.UTF_8);
            String commonResult = secondResult + firstResult;
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1,
                    OK,
                    Unpooled.copiedBuffer(commonResult, CharsetUtil.UTF_8));
            response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

            serverctx.writeAndFlush(response)
                    .addListener(ChannelFutureListener.CLOSE);
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }



}
