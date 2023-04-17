import {createSlice} from "@reduxjs/toolkit";

const cartItem = createSlice({
    name: 'cart',
    initialState: [],
    reducers: {
        insertItem(state, action) {
            const num = state.findIndex((obj) => {
                return obj.id === action.payload.id;
            });
            num === -1 ? state.push(action.payload) : state[num].quantity += action.payload.quantity;
        },
        increaseItem(state, action) {
            const nums = state.findIndex(a => a.id === action.payload);
            state[nums].quantity++;
            state[nums].totalAmount = state[nums].quantity * state[nums].price;
        },
        decreaseItem(state, action) {
            const nums = state.findIndex(a => a.id === action.payload);
            state[nums].quantity > 0 ?
                function (){
                    state[nums].quantity--;
                    state[nums].totalAmount = state[nums].quantity * state[nums].price;
                }()
                : removeItem(state, action);

        },
        removeItem(state, action) {
            const copy = [...state];
            copy.splice(action.payload, 1);
            return copy;
        }
    }
});

export const {insertItem, increaseItem, decreaseItem, removeItem} = cartItem.actions;

export default cartItem;
