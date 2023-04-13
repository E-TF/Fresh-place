import {combineReducers, configureStore} from '@reduxjs/toolkit'
import cartItem from "./cartItemSlice";
import storage from 'redux-persist/lib/storage'
import {persistReducer} from "redux-persist";

const persistConfig = {
    key: 'root',
    storage : storage,
    whitelist : ['cartItem']
}

const reducer = combineReducers({
    cartItem: cartItem.reducer
});

const persistedReducer = persistReducer(persistConfig, reducer)

export default configureStore({
    reducer: persistedReducer
})
