package com.ysh.net;

/**
 * @Author: Henry Yi
 * @Date: 7/18/2020 - 10:54
 * @Description: com.ysh.net
 * @Version: 1.0
 */
public abstract class Msg {
    public abstract byte[] toBytes();

    public abstract void parse(byte[] bytes);

    public abstract void handle();

    public abstract MsgType getMsgType();
}
