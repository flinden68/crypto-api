
if (!!window.EventSource) {
    var source = new EventSource('/crypto-api/prices');
} else {
    // Result to xhr polling :(
}

source.addEventListener('message', function(e) {
    var p = JSON.parse(e.data);
    for (var key of Object.keys(p)) {
        $("#" + key).find(".euro").text(formatCurrency(p[key].EUR));
        calculateTotal(key);
    }
}, false);

source.addEventListener('open', function(e) {
    console.log("Connection was opened");
    // .
}, false);

source.addEventListener('error', function(e) {
    if (e.readyState == EventSource.CLOSED) {
        console.log("Connection was closed");
    }
}, false);

$(document).ready(function() {
    fetchTransactions();

    $(".buy").on('click', function(){
        let coinCode = $(this).attr("coin-code");
        callTransactionService('buy', coinCode, $("#" + coinCode + "_buy").val());
    });

    $(".sell").on('click', function(){
        let coinCode = $(this).attr("coin-code");
        let amount = Number.parseFloat($("#" + coinCode + "_sell").val());
        let totalAmount = Number.parseFloat($("#" + coinCode).find(".amount").text());

        if(amount > totalAmount){
            amount = totalAmount;
        }
        callTransactionService('sell', coinCode, amount);
    });
});

function callTransactionService(type, coinCode, amount){
    let url = '/crypto-api/' + type + '/' + coinCode + '/' + amount;
    fetch(url)
        .then((resp) => resp.json())
        .then(function(data) {
            if (data) {
                updateAmount(coinCode, type, amount);
                calculateTotal(coinCode);
                appendTransaction(data);
            }
        })
        .catch(function(error) {
            console.log(error);
        });
}

function fetchTransactions(){
    let url = '/crypto-api/transactions';
    fetch(url)
        .then((resp) => resp.json())
        .then(function(data) {
            if (data) {
                data.forEach(transaction => {
                    updateAmount(transaction.currencyCode, transaction.type, transaction.amount);
                    calculateTotal(transaction.currencyCode);
                    appendTransaction(transaction);
                });
            }
        })
        .catch(function(error) {
            console.log(error);
        });
}

function updateAmount(coinCode, type, amount){
    let currentAmount = Number.parseFloat($("#" + coinCode).find(".amount").text());
    if(type.toLowerCase() === 'sell'){
        currentAmount = currentAmount - Number.parseFloat(amount);
    }else{
        currentAmount = currentAmount + Number.parseFloat(amount);
    }
    $("#" + coinCode).find(".amount").text(currentAmount);
}
function appendTransaction(data){
    $("#transactions>tbody").append(transactionRow(data));
}

function transactionRow(data){
    let created = new Date(data.created);
    row = $("<tr></tr>");
    col1 = $("<td>"+formatDate(created) + "</td>");
    col2 = $("<td>"+data.type+"</td>");
    col3 = $("<td>"+data.currencyCode+"</td>");
    col4 = $("<td>"+data.amount+"</td>");
    row.append(col1,col2,col3,col4);

    return row
}

function calculateTotal(coinCode){
    let total = Number.parseFloat($("#" + coinCode).find(".euro").text()) * Number.parseFloat($("#" + coinCode).find(".amount").text());
    $("#" + coinCode).find(".total").text(total);
}

function formatCurrency(num) {
    return (
        Number.parseFloat(num)
            .toFixed(2) // always two decimal digits
            .replace('.', ',') // replace decimal point character with ,
            .replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.')
    )
}

function formatDate(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();
    minutes = minutes < 10 ? '0'+minutes : minutes;
    seconds = seconds < 10 ? '0'+seconds : seconds;
    var strTime = hours + ':' + minutes + ':' + seconds;
    return date.getDate() + "-" + (date.getMonth()+1)  + "-" + date.getFullYear() + "  " + strTime;
}