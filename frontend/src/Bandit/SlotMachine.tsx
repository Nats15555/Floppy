import {useEffect, useRef, useState} from 'react';
import './SlotMachine.css';
import Slot from './Slot';
import gif from '../assets/b.gif';
import { Fireworks } from '@fireworks-js/react'
import type { FireworksHandlers } from '@fireworks-js/react'
import {useParams} from "react-router-dom";
import {sendBandit} from "../api/Api";

const SlotMachine = () => {
    const [spinning, setSpinning] = useState(false);
    const [pressed, setPressed] = useState(false);
    const [isPlaying, setPlaying] = useState(true);
    const [balance, setBalance] = useState(100);
    const [bet, setBet] = useState(10);
    const [balanceClass, setBalanceClass] = useState('');
    const audioRef = useRef(null);
    const newBalance = useRef(0);
    const [win, setWin] = useState(false)

    const [useData, setUseData] = useState("null");


    const counts = [25, 50, 100]
    const maxTime = 5000;
    const symbols = ["🍒", "🍋", "🍊", "🍉", "⭐️", "💎"];
    const symbols2 = [0, 1, 2, 3, 4, 5];

    const {tgId, startBalance} = useParams();

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
            tmpItems[i] = symbols2[Math.floor(Math.random() * symbols2.length)];
        }

        tmpItems[count] = last

        setItems({
            items: tmpItems,
            last: last,
            count: count,
        })

        return tmpItems
    }

    const generateRandomSym = () => {
        return [
            Math.floor(Math.random() * symbols2.length),
            Math.floor(Math.random() * symbols2.length),
            Math.floor(Math.random() * symbols2.length)
            //0, 0, 0
        ]
    }

    useEffect(() => {
        generateItems(setItems1, 1, symbols2[Math.floor(Math.random() * symbols2.length)]);
        generateItems(setItems2, 1, symbols2[Math.floor(Math.random() * symbols2.length)])
        generateItems(setItems3, 1, symbols2[Math.floor(Math.random() * symbols2.length)])

        let newBalance = Number(startBalance)

        setBalance(newBalance);

        setBet(bet => Math.min(bet, newBalance))
        toggle()

    }, []);

    const startSpin = () => {
        ref.current.stop()
        //let results = sendBandit(tgId, bet);

        let results = generateRandomSym();

        //let {balance, winIndexes} = results.data
        //console.log(winIndexes)

        if (results[0] === results[1] && results[1] === results[2]){
            newBalance.current = balance + 10 * bet;
        } else {
            newBalance.current = balance - bet;
        }

        generateItems(setItems1, counts[0], results[0])
        generateItems(setItems2, counts[1], results[1])
        generateItems(setItems3, counts[2], results[2])

        setSpinning(true);
        setPressed(true);

        // results.then(results=> {
        //
        //
        //     //setUseData(results.data)
        // })/*.catch(error => {
        //     console.log(error);
        //     if (error){
        //         setUseData(error.code + " " + error.message + error.config.data + " " + error.stack + " " + error.response)
        //     }
        // })*/

        audioRef.current.play();

        setTimeout(() => {
            setSpinning(false);
            // console.log(newBalance, balance)
            if (newBalance.current > balance){
                setBalanceClass("increase")
                ref.current.start()
                setWin(true)
            } else {
                setBalanceClass("decrease")
            }
            setBalance(newBalance.current);
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

    const ref = useRef<FireworksHandlers>(null)

    const toggle = () => {
        if (!ref.current) return
        if (ref.current.isRunning) {
            ref.current.stop()
        } else {
            ref.current.start()
        }
    }


    return (
        <div className="many-slot-machine">
            <div className="slots">
                <Slot items={items1} spinning={spinning} timeSpinning={maxTime / 3} isPlaying={isPlaying} isWin = {win}/>
                <Slot items={items2} spinning={spinning} timeSpinning={maxTime / 2} isPlaying={isPlaying} isWin = {win}/>
                <Slot items={items3} spinning={spinning} timeSpinning={maxTime / 1} isPlaying={isPlaying} isWin = {win}/>
            </div>

            <div className={`bg-div balance-div`}>
                <div className={`balance ${balanceClass}`}>
                    {balance}$
                </div>
            </div>
            <div className={`bg-div bet-div`}>
                <input
                    type="number"
                    value={bet}
                    onChange={handelChangeBet}
                    style={{width: "80px"}}
                />
            </div>

            <div className={`bg-div spin-div`}>
                <button className={`spin-button ${spinning ? 'spinning' : ''}`} onClick={startSpin} disabled={spinning}>
                    {spinning ? "Spinning..." : "Spin"}
                </button>
            </div>

            <Fireworks
                ref={ref}
                options={{opacity: 0.5}}
                style={{
                    position: "absolute",
                    top: "20px",
                    width: '350px',
                    height: '350px',
                }}
            />

            <img src={gif} onClick={() => {
                setPressed(false)
                if (ref.current.isRunning) {
                    ref.current.stop()
                }
            }} style={{
                display: `${!spinning && pressed && win ? 'flex' : 'none'}`,
                flexDirection: "column",
                alignItems: "center",
                width: "400px",
                height: "400px",
                position: "absolute",
                top: "40px"
            }}/>


            <audio ref={audioRef} loop preload="auto">
                <source src="/sounds/jok.mp3" type="audio/mpeg"/>
                Ваш браузер не поддерживает аудио.
            </audio>

        </div>

    );
};

export default SlotMachine;