import {createSlice} from "@reduxjs/toolkit";

let cartItem = createSlice({
    name: 'cart',
    initialState: [],
    reducers: {
        insertItem(state, action) {
            let num = state.findIndex((obj) => {
                return obj.id === action.payload.id;
            });
            num === -1 ? state.push(action.payload) : state[num].quantity += action.payload.quantity;
        },
        increaseItem(state, action) {
            let nums = state.findIndex(a => a.id === action.payload);
            state[nums].quantity++;
            state[nums].totalAmount = state[nums].quantity * state[nums].price;
        },
        decreaseItem(state, action) {
            let nums = state.findIndex(a => a.id === action.payload);
            state[nums].quantity > 0 ?
                function (){
                    state[nums].quantity--;
                    state[nums].totalAmount = state[nums].quantity * state[nums].price;
                }()
                : removeItem(state, action);

        },
        removeItem(state, action) {
            let copy = [...state];
            copy.splice(action.payload, 1);
            return copy;
        }
    }
});

export let {insertItem, increaseItem, decreaseItem, removeItem} = cartItem.actions;

export default cartItem;
