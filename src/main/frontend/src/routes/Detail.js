import {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";

function Detail() {

    let [item, setItem] = useState([]);
    let {itemSeq} = useParams();
    let [quantity, setQuantity] = useState(1);
    let totalAmount = useState(0);

    useEffect(() => {
        function getItemDetail() {
            axios.get('/public/item/' + itemSeq)
                .then(data => {
                    let copy = data.data;
                    setItem(copy);
                })
        }

        getItemDetail()
        totalAmount = item.price * quantity;
    }, [itemSeq])

    const decreaseQuantity = () =>
        setQuantity(--quantity)

    const increaseQuantity = () =>
        setQuantity(++quantity)

    return (
        <>
            <div className='standard-area'>

                <div className="container">
                    <div className="row">

                        <div>
                            <div style={{float: 'left'}} aria-setsize='50%'>
                                {
                                    item.imageUrlList && <img src={item.imageUrlList[0]} width='100%' height='80%'/>
                                }
                            </div>
                            <div style={{float: 'right'}} aria-setsize='50%'>

                                <div>
                                    <h2>{item.itemName}</h2>
                                    <p>{item.description}</p>
                                </div>

                                <br/>

                                <div>
                                    <h2>{item.price}</h2>
                                </div>

                                <hr/>

                                <div>
                                    <p>중량/용량 {item.weight}</p>
                                    <p>원산지 {item.origin}</p>
                                </div>

                                <hr/>

                                <div>
                                    <p>구매수량
                                        <strong onClick={() => {decreaseQuantity()}}>-</strong>
                                        <em>{quantity}</em>
                                        <strong onClick={() => {increaseQuantity()}}>+</strong>
                                    </p>
                                </div>

                                <hr/>

                                <div>
                                    <p>충 상픔 금액 : <strong>{totalAmount}</strong></p>
                                </div>

                                <div>
                                    <button>바로구매</button>
                                    <button>장바구니</button>
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
                                    <img src={a} width='100%' height='100%'/>
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
