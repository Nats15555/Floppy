import {useEffect, useRef, useState} from "react";
import ManySlot from "./ManySlot";
import "./ManySlot.css"

const ManySlotsMachine = () => {
    const [spinning, setSpinning] = useState(false);
    const maxTime = 5000;
    const symbols = [0, 1, 2, 3, 4]
    const [winMatrix, setWinMatrix] = useState(  [[0, 1, 2, 3, 4], [1, 2, 3, 4, 0], [2, 3, 4, 1, 0]]);
    const [winCombo, setWinCombo] = useState([[0, 1, 1, 0, 0], [1, 1, 1, 1, 1], [0, 1, 1, 0, 0]]);
    const [pressed, setPressed] = useState(false)
    const [balance, setBalance] = useState(100);
    const [bet, setBet] = useState(10);
    const [balanceClass, setBalanceClass] = useState('');

    const audioRef = useRef(null);

    const [items1, setItems1] = useState({
        items: [],
        last: "",
        count: 0
    });
    const [items2, setItems2] = useState({
        items: [],
        last: "",
        count: 0
    });
    const [items3, setItems3] = useState({
        items: [],
        last: "",
        count: 0
    });

    const generateItems = (setItems, count, last) => {
        let tmpItems = []

        for (let i = 0; i < count; i++) {
            tmpItems[i] = symbols[Math.floor(Math.random() * symbols.length)];
        }

        for (let i = 0; i < last.length; i++) {
            tmpItems[count + i] = last[i]
        }

        setItems({
            items: tmpItems,
            last: last,
            count: count,
        })

        console.log(tmpItems)
    }

    useEffect(() => {

        generateItems(setItems1, 25, winMatrix[0])
        generateItems(setItems2, 50, winMatrix[1])
        generateItems(setItems3, 100, winMatrix[2])

        audioRef.current.play();

    }, []);

    const startSpin = () => {
        setSpinning(true);
        setPressed(true);
        audioRef.current.play()
        generateItems(setItems1, 25, winMatrix[0])
        generateItems(setItems2, 50, winMatrix[1])
        generateItems(setItems3, 100, winMatrix[2])

        setTimeout(() => {
            setSpinning(false);
            setBalanceClass("decrease") //increase / decrease
            setBalance(balance => balance + Number(bet));
        }, maxTime);

    };

    useEffect(() => {
        const timer = setTimeout(() => {
            setBalanceClass('');
        }, 1000);

        return () => clearTimeout(timer);
    }, [balance]);


    const handelChangeBet = (e) => {
        let value = e.target.value;
        const intV = value.replace(/^0+\d/, '');
        if (intV >= 0 && intV <= balance){
            setBet(intV);
        }

        if (value === ""){
            setBet(0);
        }
    }

    return (<div className="many-slot-machine">
        <div className="many-slots">
            <ManySlot items={items1} spinning={spinning} timeSpinning={maxTime / 3} winCombo={winCombo[0]}
                      pressed={pressed}/>
            <ManySlot items={items2} spinning={spinning} timeSpinning={maxTime / 2} winCombo={winCombo[1]}
                      pressed={pressed}/>
            <ManySlot items={items3} spinning={spinning} timeSpinning={maxTime / 1} winCombo={winCombo[2]}
                      pressed={pressed}/>
        </div>

        <div className={`many-spin ${spinning ? 'many-spin-anim' : ""}`}>
            <button onClick={startSpin} disabled={spinning}>
                {spinning ? "Spinning..." : "Spin"}
            </button>
        </div>
        <div className={`many-balance ${balanceClass}`}>
            Balance: {balance}$
        </div>

        <img src="/imgs/fruit.png" className="fruit-name"/>

        <div className={`many-bet`}>
            <input
                type="number"
                value={bet}
                onChange={handelChangeBet}
                style={{width: "80px"}}
            />
        </div>

        <audio ref={audioRef} loop>
            <source src="/sounds/fruit.mp3" type="audio/mpeg"/>
            Ваш браузер не поддерживает аудио.
        </audio>
    </div>);
}

export default ManySlotsMachine;