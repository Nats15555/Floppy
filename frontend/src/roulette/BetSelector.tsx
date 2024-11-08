import './BetBoard.css';

const BetBoard = ({ data, selectedBet, setSelectedBet}) => {

    const handleNumberClick = (number) => {
        setSelectedBet(number);
    };

    const handleColorClick = (color) => {
        setSelectedBet(color);
    };

    const handleOddEvenClick = (type) => {
        setSelectedBet(type);
    };

    const renderNumberCells = () => {
        return (
            <div className="number-board">
                {data.slice(1, data.length).map((number) => (
                    <button
                        style={{
                            background: `${number.style.backgroundColor}`,
                            color: `white`
                        }}
                        key={number.option}
                        className={`bet-button ${number.option % 2 === 0 ? 'even' : 'odd'} ${selectedBet === number.option ? 'selected' : ''}`}
                        onClick={() => handleNumberClick(number.option)}
                    >
                        {number.option}
                    </button>
                ))}
            </div>
        );
    };

    return (
        <div className="bet-board">
            <h2>Выбор ставки для Spin Wheel</h2>
            <h3>Выбранная ставка: {selectedBet}</h3>
            <div className="bet-options">
                <div className="color-options">
                    <button className={`bet-button red ${selectedBet === 'red' ? 'selected' : ''}`}
                            onClick={() => handleColorClick('red')}>
                        Красный
                    </button>
                    <button className={`bet-button black ${selectedBet === 'black' ? 'selected' : ''}`}
                            onClick={() => handleColorClick('black')}>
                        Чёрный
                    </button>
                    <button className={`bet-button zero ${selectedBet === "0" ? 'selected' : ''}`}
                            onClick={() => handleNumberClick(0)}>
                        0
                    </button>
                </div>
                <div className="odd-even-options">
                    <button className={`bet-button even ${selectedBet === 'even' ? 'selected' : ''}`}
                            onClick={() => handleOddEvenClick('even')}>
                        Чётное
                    </button>
                    <button className={`bet-button odd ${selectedBet === 'odd' ? 'selected' : ''}`}
                            onClick={() => handleOddEvenClick('odd')}>
                        Нечётное
                    </button>
                </div>
            </div>
            {renderNumberCells()}
        </div>
    );
};

export default BetBoard;