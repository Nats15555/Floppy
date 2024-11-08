import {useRef, useState} from 'react'
import {Wheel} from 'react-custom-roulette'
import {PointerProps} from "react-custom-roulette/dist/components/Wheel/types";
import BetSelector from "./BetSelector";
import {wheelSeqData} from "./WheelData";

const bgColor = [
    "red",
    "black",
    "green"
]

const pointer: PointerProps = {
    src: "/imgs/pointer.png",
}

function generateList() {
    return wheelSeqData;

}

const sortData = (tmpData) => {
    return [...tmpData].sort((a, b) => a.option - b.option);
}

const data2 = generateList();
const data3 = sortData(data2)


const SpinMachine = () => {
    const audioRef = useRef(new Audio('/sounds/wheel.mp3'));
    const winRef = useRef(new Audio('/sounds/winWheel.mp3'));
    const loseRef = useRef(new Audio('/sounds/loseWheel.mp3'));
    const [selectedBet, setSelectedBet] = useState(0)
    const isWin = useRef(false);

    const [mustSpin, setMustSpin] = useState(false);
    const [prizeNumber, setPrizeNumber] = useState(Math.floor(Math.random() * data2.length));

    const handleSpinClick = () => {
        console.log(selectedBet)
        if (!mustSpin) {
            audioRef.current.play();
            isWin.current = checkPrizeWin(prizeNumber, selectedBet)

            setMustSpin(true);
            setPrizeNumber(Math.floor(Math.random() * data2.length))

        }

    }

    const checkPrizeWin = (prizeRes, set) => {
        console.log(prizeRes + " " + set + " " + data2[prizeRes].option)
        if (set === "red" && data2[prizeRes].style.backgroundColor === "red") {
            return true
        } else if (set === "black" && data2[prizeRes].style.backgroundColor === "black") {
            return true
        } else if (set === "odd" && Number(data2[prizeRes].option) % 2 !== 0) {
            return true;
        } else if (set === "even" && Number(data2[prizeRes].option) % 2 === 0) {
            return true;
        } else if (String(data2[prizeRes].option) === String(set)) {
            return true;
        }

        return false;

    }

    const handleStop = () => {
        setMustSpin(false)
        audioRef.current.pause()
        if (isWin.current) {
            winRef.current.play()
        } else {
            loseRef.current.play()
        }
        isWin.current = false;
    }

    return (
        <>
            <Wheel
                mustStartSpinning={mustSpin}
                prizeNumber={prizeNumber}
                outerBorderColor={"black"}
                outerBorderWidth={5}
                data={data2}
                radiusLineWidth={2}
                innerRadius={35}
                innerBorderColor="black"
                radiusLineColor="white"
                innerBorderWidth={6}
                spinDuration={2}
                perpendicularText={true}
                onStopSpinning={handleStop}
                textDistance={80}
                backgroundColors={bgColor}
                textColors={["white"]}
                fontSize={15}
                pointerProps={pointer}
            />
            <BetSelector data={data3} selectedBet={selectedBet} setSelectedBet={setSelectedBet}/>
            <div
                style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    justifySelf: "center"
                }}>
                <button onClick={handleSpinClick} disabled={mustSpin}>
                    {mustSpin ? "Spinning..." : "Spin"}
                </button>
            </div>

        </>
    )
}
export default SpinMachine;