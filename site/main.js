function myJavaScript() {
    var database = firebase.database();

    var table = document.getElementById("mPrices");
    var mWolf = document.getElementById("mWolf");
    var mAllTimeWolf = document.getElementById("mAllTimeWolf");

    var maxOrder;
    var maxOrderName;
    var allTimeWolf;
    var allTimeWolfName;

    database.ref("maxOrder").once("value", function(snapshot) {
        maxOrder = snapshot.val();
    });
    database.ref("maxOrderName").once("value", function(snapshot) {
        maxOrderName = snapshot.val();
    });

    database.ref("allTimeWolf").once("value", function(snapshot) {
        allTimeWolf = snapshot.val();
    });
    database.ref("allTimeWolfName").once("value", function(snapshot) {
        allTimeWolfName = snapshot.val();
    });

    // Drinks
    database.ref("Drinks").on("child_added", function(drinkSnapshot) {
        var row = table.insertRow(-1);
        var mName = row.insertCell(0);
        var mPrice = row.insertCell(1);
        var mPriceDifference = row.insertCell(2);

        // Name
        var name = drinkSnapshot.child("name").val();
        if (name == "Stella") {
            mName.innerHTML = name.bold();
        }
        else {
            mName.innerHTML = name;
        }
    
        // PriceDifference
        database.ref("Drinks/" + name + "/priceDifference").on("value", function(snapshot) {
            priceDifference = snapshot.val().toFixed(2);
            if (priceDifference < 0) {
                mPriceDifference.innerHTML = priceDifference;
                mPriceDifference.style.color = "#43A047";
            } 
            else if (priceDifference > 0) {
                mPriceDifference.innerHTML = "+" + priceDifference;
                mPriceDifference.style.color = "#BF0C0C";
            } 
            else {
                mPriceDifference.innerHTML = "+" + priceDifference;
                mPriceDifference.style.color = "#757575";
            }
        });

        // Price
        database.ref("Drinks/" + name + "/price").on("value", function(snapshot) {
            var price = snapshot.val().toFixed(2);
            if (price > 0) {
                mPrice.innerHTML = "€ " + price;
            } else {
                mPrice.innerHTML = "SOLD";
                mPriceDifference.innerHTML = "OUT";
            }
        });
    });

    database.ref("Drinks").on("child_removed", function(snapshot) {
        var name = snapshot.ref("name").val();
        for (i=1; i<table.rows.length; i++) {
            if (table.rows[i].cell[0]) {
                table.deleteRow(i);
                break;
            }
        }
    });

    // maxOrder
    database.ref("maxOrder").on("value", function(snapshot) {
        maxOrder = snapshot.val();
        mWolf.innerHTML = "Wolf of Capital Booze:\n" + maxOrderName.bold() + "\n" + maxOrder + " drinks";
    });

    // maxOrderName
    database.ref("maxOrderName").on("value", function(snapshot) {
        maxOrderName = snapshot.val();
        mWolf.innerHTML = "Wolf of Capital Booze:\n" + maxOrderName.bold() + "\n" + maxOrder + " drinks";
    });

    // allTimeWolf
    database.ref("allTimeWolf").on("value", function(snapshot) {
        allTimeWolf = snapshot.val();
        mAllTimeWolf.innerHTML = "All time wolf:\n" + allTimeWolfName.bold() + "\n" + allTimeWolf + " drinks";
    });

    // allTimeWolfName
    database.ref("allTimeWolfName").on("value", function(snapshot) {
        allTimeWolfName = snapshot.val();
        mAllTimeWolf.innerHTML = "All time wolf:\n" + allTimeWolfName.bold() + "\n" + allTimeWolf + " drinks";
    });
}