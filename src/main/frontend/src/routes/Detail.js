import {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import {Table} from "react-bootstrap";
import Button from "react-bootstrap/Button";

function Detail() {

    const {itemSeq} = useParams();
    let [item, setItem] = useState([]);
    let [quantity, setQuantity] = useState(1);
    let [totalAmount, setTotalAmount] = useState(0);

    useEffect(() => {
        axios.get(`/public/item/${itemSeq}`)
            .then(data => {
                let copy = data.data;
                setItem(copy);
                setTotalAmount(copy.price * quantity);
            });
    }, [itemSeq])

    const decreaseQuantity = () => {
        if (quantity === 0) {
            return;
        }
        setQuantity(--quantity);
        setTotalAmount(item.price * quantity)
    };

    const increaseQuantity = () => {
        setQuantity(++quantity);
        setTotalAmount(item.price * quantity);
    }

    return (
        <>
            <div className='standard-area'>

                <div className="container">
                    <div className="row">
                        <div>
                            <div style={{float: 'left'}}>
                                {
                                    item.imageUrlList && <img src={item.imageUrlList[0]}/>
                                }
                            </div>
                            <div style={{float: 'right'}}>
                                <div>
                                    <h2>{item.itemName}</h2>
                                    <p>{item.description}</p>
                                </div>

                                <br/>

                                <div>
                                    <h2>{item.price}원</h2>
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
                                        <td><strong onClick={() => {
                                            decreaseQuantity()
                                        }}> - </strong>
                                            <em>{quantity}</em>
                                            <strong onClick={() => {
                                                increaseQuantity()
                                            }}> + </strong></td>
                                    </tr>
                                </Table>

                                <div>
                                    <p>충 상픔 금액 : <strong>{totalAmount}</strong></p>
                                </div>

                                <br/><br/><br/><br/><br/>

                                <div>
                                    <Button variant="info">장바구니 담기</Button>{' '}
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
                                    <img src={a}/>
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
