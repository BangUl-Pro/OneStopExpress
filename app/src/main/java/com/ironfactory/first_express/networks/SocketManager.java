package com.ironfactory.first_express.networks;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.ironfactory.first_express.entities.OptionEntity;
import com.ironfactory.first_express.entities.ProductEntity;
import com.ironfactory.first_express.entities.PersonEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ironFactory on 2015-08-03.
 */
public class SocketManager {

    private static Handler handler = new Handler();
    private static final String SERVER_URL = "http://express-1st.herokuapp.com";
    private static final String TAG = "SocketManager";

    public static final String CODE = "code";
    public static final String SIGN_UP = "signUp";
    public static final String LOGIN = "login";
    public static final String INSERT_MOVE_BY_ROOM = "insertMoveByRoom";
    public static final String INSERT_MOVE_BY_PRODUCT = "insertMoveByProduct";


    public static final String NAME = "name";
    public static final String PROPERTY = "property";
    public static final String PHONE = "phone";
    public static final String ROOM = "room";
    public static final String PERSON = "person";
    public static final String PRICE = "price";
    public static final String CONTENT = "content";


    public static final int SUCCESS = 200;


    public static Socket socket;
    private static Context context;

    public SocketManager(Context context) {
        this.context = context;
        init();
    }

    public static void createInstance(Context context) {
        SocketManager.context = context;
        init();
    }

    private static void init() {
        Log.d(TAG, "init");
        try {
            socket = IO.socket(SERVER_URL);
        } catch (Exception e) {
            Log.e(TAG, "init 에러 = " + e.getMessage());
        }

        if (socket != null) {
            socketConnect();
        }
    }

    private static void checkSocket() {
        if (socket == null) {
            init();
        }
    }


    private void setListener() {
        Log.d(TAG, "setListener");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // 연결
                Log.d(TAG, "소켓 연결");
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // 연결 끊김
                Log.d(TAG, "소켓 연결 끊김");
                socketConnect();
            }
        });
    }


    public static Socket getSocket() {
        return socket;
    }


    private static void socketConnect() {
        socket.open();
        socket.connect();
    }


    public static void signUp(String name, String phone, final RequestListener.OnSignUp onSignUp) {
        try {
            checkSocket();

            JSONObject object = new JSONObject();
            object.put(NAME, name);
            object.put(PHONE, phone);
//            socket.emit("createMembers", "");
            socket.emit(SIGN_UP, object);
            socket.once(SIGN_UP, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject resObject = (JSONObject) args[0];
                                int code = resObject.getInt(CODE);
                                if (code == SUCCESS) {
                                    String curName = resObject.getString(NAME);
                                    String curPhone = resObject.getString(PHONE);
                                    onSignUp.onSuccess(curName, curPhone);
                                } else {
                                    onSignUp.onException(code);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void login(String name, String phone, final RequestListener.OnLogin onLogin) {
        try {
            checkSocket();

            JSONObject object = new JSONObject();
            object.put(NAME, name);
            object.put(PHONE, phone);
//            socket.emit("dropMoves", "");
//            socket.emit("createMoves", "");
//            socket.emit("dropMembers", "");
//            socket.emit("createMembers", "");
            socket.emit(LOGIN, object);
            socket.once(LOGIN, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject resObject = (JSONObject) args[0];
                                int code = resObject.getInt(CODE);
                                if (code == SUCCESS) {
                                    String curName = resObject.getString(NAME);
                                    String curPhone = resObject.getString(PHONE);
                                    onLogin.onSuccess(curName, curPhone);
                                } else {
                                    onLogin.onException();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void insertMove(PersonEntity personEntity, String phone, final RequestListener.OnInsertMove onInsertMove) {
        try {
            checkSocket();

            JSONObject object = new JSONObject();
            object.put(ROOM, personEntity.getRoomNum());
            object.put(PERSON, personEntity.getNum());
            object.put(PRICE, personEntity.getPrice());
            object.put(PHONE, phone);

            socket.emit("createMoves", "");
            socket.emit(INSERT_MOVE_BY_ROOM, object);
            socket.once(INSERT_MOVE_BY_ROOM, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject resObject = (JSONObject) args[0];
                                int code = resObject.getInt(CODE);
                                if (code == SUCCESS) {
                                    onInsertMove.onSuccess();
                                } else {
                                    onInsertMove.onException();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void insertMove(ArrayList<ProductEntity> productEntities, ArrayList<OptionEntity> optionEntities, int price, String phone, final RequestListener.OnInsertMove onInsertMove) {
        try {
            checkSocket();

            JSONObject reqObject = new JSONObject();
            for (ProductEntity product :
                    productEntities) {
                reqObject.put(product.getProperty(), product.getCount());
            }

            for (OptionEntity option :
                    optionEntities) {
                reqObject.put(option.getProperty(), 1);
            }
            reqObject.put(PHONE, phone);
            reqObject.put(PRICE, price);

            socket.emit(INSERT_MOVE_BY_PRODUCT, reqObject);
            socket.once(INSERT_MOVE_BY_PRODUCT, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject resObject = (JSONObject) args[0];
                                int code = resObject.getInt(CODE);
                                if (code == SUCCESS) {
                                    onInsertMove.onSuccess();
                                } else {
                                    onInsertMove.onException();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
