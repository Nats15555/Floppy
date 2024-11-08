import axios from 'axios'

export const enum Bet {
    ODD,
    EVEN,
    RED,
    BLACK,
    ZERO
}

const url = "https://176.117.196.29"
const apiUrl = "/api/v1/game"

const fullUrl = url + apiUrl

export const sendBandit = async (tgId, bet) => {
    return await axios.post(fullUrl + "/bandit/", {tgUserId: tgId, bet: bet})
}

export const sendRouletteString = (tgId, bet) => {
    axios.post(fullUrl+ "/roulette/string", {tgUserId: tgId, bet: bet}).then((response) => {
        return response.data;
    }).catch((error) => {
        console.log(error);
    })
}

export const sendRouletteNumber = (tgId, bet) => {
    axios.post(fullUrl + "/roulette/number", {tgUserId: tgId, bet: bet}).then((response) => {
        return response.data;
    }).catch((error) => {
        console.log(error);
    })
}

export const sendSlots = (tgId, bet) => {
    axios.post(fullUrl + "/slots", {tgUserId: tgId, bet: bet}).then((response) => {
        return response.data;
    }).catch((error) => {
        console.log(error);
    })
}