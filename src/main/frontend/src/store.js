import {configureStore, createSlice} from '@reduxjs/toolkit'

let user = createSlice({
    name: 'user',
    initialState : { name : 'kim', age : 20},
    reducers : {
        changeName(state) {
            state.name = 'park'
        }
    }
})

export let { changeName, } = user.actions

let cartItem = createSlice({
    name : 'cartItem',
    initialState : [
        {id : 0, name : 'White and Black', count : 2},
        {id : 2, name : 'Grey Yordan', count : 1},
    ]
})

let stock = createSlice({
    name : 'stock',
    initialState : [10, 11, 12]
})

export default configureStore({
    reducer: {
        user : user.reducer,
        stock : stock.reducer,
        cartItem : cartItem.reducer
    }
})
