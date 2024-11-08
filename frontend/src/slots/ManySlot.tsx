import {useEffect, useState} from 'react';
import styled, {keyframes} from 'styled-components';

const imgPath = "/fruits/";

const ManySlot = ({items, spinning, timeSpinning, winCombo, pressed}) => {
    const spinAnimation = () => keyframes`
        0% {
            transform: translateY(0);
        }
        50% {
            transform: translateY(-${20 * (items.count)}px);
        }
        75% {
            transform: translateY(-${35 * (items.count)}px);
        }
        90% {
            transform: translateY(-${42 * (items.count)}px);
        }
        100% {
            transform: translateY(-${43 * (items.count)}px);
        }
    `;

    const ManySlotItems = styled.div`
        transition: transform ${timeSpinning / 1000}s ease-out;

        &.spinning {
            animation: ${() => spinAnimation()} ${timeSpinning / 1000}s linear;
        }
    `;

    const ManySlotItem = styled.div`
        &.shaking {
            animation: tilt-n-move-shaking 0.5s ease infinite;
            transform: translateY(-${(items.count) * 43}px)
        }
    `

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

    useEffect(() => {
        console.log(" asd as: " + winCombo)
    }, []);

    return (
        <div className="many-slot">
            <ManySlotItems
                className={animationClass}
                itemsCount={items.items.length}
                style={{
                    transform: `translateY(-${position * 43}px)`,
                }}
            >
                {items.items.map((item, index) => (
                    <ManySlotItem key={index} className={`many-slot-item ${!pressed || spinning ? '' : index >= items.count && winCombo[index - items.count] == 1 ? 'many-shaking' : 'many-lose'} `}>
                        <img src={`${imgPath}${item}.png`} style={{
                            width: "43px",
                            height: "43px",
                        }}/>
                    </ManySlotItem>
                ))}
            </ManySlotItems>
        </div>
    );
};

export default ManySlot;