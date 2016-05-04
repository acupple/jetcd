package org.mokey.acupple.jetcd;

public interface Handler<E> {
    void handle(E event);
}
