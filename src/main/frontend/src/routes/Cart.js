import Container from "react-bootstrap/Container";
import {useDispatch, useSelector} from "react-redux";
import {Table} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {useNavigate} from "react-router-dom";
import {decreaseItem, increaseItem, removeItem} from "../store/cartItemSlice";

export const formatPrice = (target) => {
    if (target) {
        return target.toLocaleString('ko-KR');
    }
}

function Cart() {

    let items = useSelector((state) => state.cartItem);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    return (
        <>
            <div className="standard-area" style={{paddingLeft: "20%", paddingRight: "20%"}}>
                <Container className="panel">
                    <div>
                        <h1>장바구니</h1>
                    </div>
                    <hr/>
                    <div>
                        {items.length > 0 ?
                            <Table>
                                <tbody>
                                {
                                    items.map((item, i) => {
                                        return <CartItem key={i} item={item} dispatch={dispatch}/>
                                    })
                                }
                                </tbody>
                            </Table>
                            :
                            <>
                                <p>장바구니가 비어있습니다.</p>
                                <Button onClick={navigate('/')}>쇼핑하러 가기</Button>
                            </>
                        }
                    </div>

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
                    <strong onClick={() => {
                        dispatch(decreaseItem(item.id));
                    }}> - </strong>
                    <em>{item.quantity}</em>
                    <strong onClick={() => {
                        dispatch(increaseItem(item.id));
                    }}> + </strong>
                </td>
                <td>{formatPrice(item.totalAmount)}원</td>
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
