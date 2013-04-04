package com.whiuk.philip.game.shared;

//TODO Build from protobuf

/**
 * @author Philip
 *
 */
public class Message {

    /**
     * @author Philip
     *
     */
    public interface Data {

    }
    /**
     * @author Philip
     *
     */
    public class AccountData implements Data {
        /**
         * @author Philip
         *
         */
        public enum Type {
            /**
             * 
             */
            LOGIN,
            /**
             * 
             */
            LOGOUT
        }
        
        private Type type;
        private LoginAttempt loginAttempt;
        
        /**
         * @return attempt
         */
        public LoginAttempt getLoginAttempt() {
            return loginAttempt;
        }

        /**
         * @author Philip
         *
         */
        public class LoginAttempt {

        }

        /**
         * @return type
         */
        public Type getType() {
            // TODO Auto-generated method stub
            return type;
        }
    }

    /**
     * @author Philip
     *
     */
    public enum Type {
        AUTH, GAME, CHAT, SYSTEM

    }

    private Data data;
    private Type type;
    private Source src;

    /**
     * @return
     */
    public final Type getType() {
        // TODO Auto-generated method stub
        return type;
    }

    /**
     * @return
     */
    public final Data getData() {
        return data;
    }

    public Source getSource() {
        // TODO Auto-generated method stub
        return src;
    }

}
