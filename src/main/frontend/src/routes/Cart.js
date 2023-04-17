import Container from "react-bootstrap/Container";
import {useDispatch, useSelector} from "react-redux";
import {Table} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {decreaseItem, increaseItem, removeItem} from "../store/cartItemSlice";

export const formatPrice = (target) => {
    if (target) {
        return target.toLocaleString('ko-KR');
    }
}

function Cart(props) {

    const items = useSelector((state) => state.cartItem);
    const dispatch = useDispatch();

    return (
        <>
            <div className="standard-area" style={{paddingLeft: "20%", paddingRight: "20%"}}>
                <div>
                    <h1>장바구니</h1>
                </div>
                <Container className="panel">
                    {items.length > 0 ?
                        <div style={{float: 'left'}}>
                            <hr/>
                            <Table>
                                <tbody>
                                {
                                    items.map((item, i) => {
                                        return <CartItem key={i} item={item} dispatch={dispatch}/>
                                    })
                                }
                                </tbody>
                            </Table>
                        </div>
                        :
                        <>
                            <p>장바구니가 비어있습니다. </p>
                            <Button variant="info" onClick={() => props.navigate('/')}>쇼핑하러 가기</Button>
                        </>
                    }
                </Container>
            </div>
        </>
    )
}

const CartItem = (props) => {

    const item = props.item;
    const dispatch = props.dispatch;

    return (
        <>
            <tr>
                <td><img src={item.thumbnail} width={'30px'}/></td>
                <td>{item.name}</td>
                <td>
                    <Button variant="outline-secondary" onClick={() => {
                        dispatch(decreaseItem(item.id));
                    }}><strong>-</strong></Button>{'  '}
                    <em>{item.quantity}</em>{'  '}
                    <Button variant="outline-primary" onClick={() => {
                        dispatch(increaseItem(item.id));
                    }}><strong>+</strong></Button>
                </td>
                <td>{item.totalAmount && formatPrice(item.totalAmount)}원</td>
                <td>
                    <strong onClick={(e) => {
                        dispatch(removeItem(item.id));
                    }}>x</strong>
                </td>
            </tr>
        </>
    )
}

export default Cart;
