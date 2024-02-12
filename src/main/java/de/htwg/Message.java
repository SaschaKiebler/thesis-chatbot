package de.htwg;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.UUID;

@Entity
public class Message {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String message;
    private Date date;
    private String model;

    public Message() {
    }

    private Message(MessageBuilder messageBuilder){
        this.message = messageBuilder.message;
        this.date = messageBuilder.date;
        this.model = messageBuilder.model;
    }

    public static class MessageBuilder{
        private String message;
        private Date date;
        private String model;

        public MessageBuilder() {
            this.date = new Date();
        }


        public MessageBuilder message(String message){
            this.message = message;
            return this;
        }

        public MessageBuilder date(Date date){
            this.date = date;
            return this;
        }

        public MessageBuilder model(String model){
            this.model = model;
            return this;
        }

        public Message build(){
            return new Message(this);
        }
    }


}
