import {useEffect, useState} from 'react';
import styled, {keyframes} from 'styled-components';

const imgPath = "/imgs/";
const gifPath = "/gifs/";

const spinAnimation = (count) => keyframes`
        0% {
            transform: translateY(0);
        }
        50% {
            transform: translateY(-${70 * count}px);
        }
        75% {
            transform: translateY(-${90 * count}px);
        }
        90% {
            transform: translateY(-${99 * count}px);
        }
        100% {
            transform: translateY(-${100 * count}px);
        }
    `;

const SlotItems = styled.div`
        transition: transform ${props => props.timespinning / 1000}s ease-out;

        &.spinning {
            animation: ${props => spinAnimation(props.itemscount)} ${props => props.timespinning / 1000}s linear;
        }
    `;

const SlotItem = styled.div`
        &.shaking {
            animation: tilt-n-move-shaking 0.5s ease infinite;
            transform: translateY(-${props => props.itemscount * 100}px)
        }
    `

const SlotMachine = ({items, spinning, timeSpinning, isPlaying, isWin}) => {

    const [position, setPosition] = useState(0);
    const [animationClass, setAnimationClass] = useState('');
    const [shaking, setShaking] = useState("");

    useEffect(() => {
        if (spinning) {
            setAnimationClass('spinning');
            setShaking('')

            setTimeout(() => {
                setAnimationClass('');
                setPosition(items.count);
                setShaking('shaking')
            }, timeSpinning);
        }
    }, [spinning]);

    return (
        <div className="slot">
            <SlotItems
                className={animationClass}
                itemscount={items.count}
                timespinning={timeSpinning}
                style={{
                    transform: `translateY(-${position * 100}px)`,
                }}
            >
                {items.items.map((item, index) => (
                    <SlotItem key={index} className={`slot-item`} itemscount={items.count}>
                        {isPlaying && shaking && !spinning && isWin? (
                            <img src={`${gifPath}${item}.gif`} width="100px" height="100px"/> //todo import to load it faster!
                        ) : (
                            <img src={`${imgPath}${item}.png`} width="100px" height="100px"/>
                        )}
                    </SlotItem>
                ))}
            </SlotItems>
        </div>
    );
};

export default SlotMachine;