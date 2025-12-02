package com.purityvanilla.pvperks.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class CustomTagResolvers {

    public static TagResolver badgeResolver(String badgeName) {
        return TagResolver.resolver(Placeholder.component("badge", Component.text(badgeName)));
    }
}