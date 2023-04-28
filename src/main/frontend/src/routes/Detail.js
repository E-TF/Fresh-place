import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import {Table} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {formatPrice} from "./Cart";
import {useDispatch} from "react-redux";
import {insertItem} from "../store/cartItemSlice";

function Detail() {

    const {itemSeq} = useParams();
    const [item, setItem] = useState([]);
    const [quantity, setQuantity] = useState(1);
    const [totalAmount, setTotalAmount] = useState(0);
    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        axios.get(`/public/item/${itemSeq}`)
            .then(data => {
                const copy = data.data;
                setItem(copy);
            })
            .catch(() => {
                alert('존재하지 않는 상품입니다.');
                navigate(-1);
            });
    }, [itemSeq, navigate]);

    const decreaseQuantity = () => {
        if (quantity <= 1) {
            return;
        }
        setQuantity(quantity - 1);
    };

    const increaseQuantity = () => {
        setQuantity(quantity + 1);
    };

    useEffect(() => {
        setTotalAmount(item.price * quantity);
    }, [item, quantity]);

    const addCartItem = () => {
        dispatch(insertItem({
            id: itemSeq,
            name: item.itemName,
            thumbnail: item.imageUrlList[0],
            price: item.price,
            quantity: quantity,
            totalAmount: totalAmount
        }));
    };

    return (
        <>
            <div className='standard-area'>

                <div className="container">
                    <div className="row">
                        <div>
                            <div style={{float: 'left'}}>
                                {
                                    item.imageUrlList && <img alt={'thumbNail'} src={item.imageUrlList[0]}/>
                                }
                            </div>
                            <div style={{float: 'right'}}>
                                <div>
                                    <h2>{item.itemName}</h2>
                                    <p>{item.description}</p>
                                </div>

                                <br/>

                                <div>
                                    <h2>{formatPrice(item.price)}원</h2>
                                </div>
                                <hr/>

                                <Table>
                                    <tr>
                                        <td>중량/용량</td>
                                        <td>{item.weight}</td>
                                    </tr>
                                    <tr>
                                        <td>원산지</td>
                                        <td>{item.origin}</td>
                                    </tr>
                                    <tr>
                                        <td>유통기한</td>
                                        <td>{item.expirationDate}</td>
                                    </tr>
                                    <tr>
                                        <td>구매수량</td>
                                        <td>
                                            <Button variant="outline-secondary" onClick={() => {
                                                decreaseQuantity()
                                            }}><strong>-</strong></Button>{' '}
                                            <em>{quantity}</em>
                                            <Button variant="outline-primary" onClick={() => {
                                                increaseQuantity()
                                            }}><strong>+</strong></Button>{' '}
                                        </td>
                                    </tr>
                                </Table>

                                <div>
                                    <p>충 상픔 금액 : <strong>{formatPrice(totalAmount)}원</strong></p>
                                </div>

                                <br/><br/><br/><br/><br/>

                                <div className="d-grid gap-2">
                                    <Button variant="info" size={"lg"} onClick={() => addCartItem()}>장바구니
                                        담기</Button>{' '}
                                </div>
                            </div>

                        </div>

                    </div>
                </div>

                <br/><br/><br/><br/><br/>

                <div className="container">

                    {
                        item.imageUrlList &&
                        item.imageUrlList.map((a) =>

                            <>
                                <div className="row">
                                    <img alt={'imageList'} src={a}/>
                                </div>
                            </>
                        )

                    }

                </div>

            </div>
        </>

    )
}

export default Detail;
