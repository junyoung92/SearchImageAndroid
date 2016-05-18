package com.example.qualson_kjy.search.model;

import java.io.Serializable;

/**
 * Created by Qualson_KJY on 2016-05-04.
 */
public class ChannelRoot implements Serializable {
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
