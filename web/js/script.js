function viewLLPopup(url) {

    var w = 700;
    var h = 900;
    var winl = (screen.width-w)/2;
    var wint = (screen.height-h)/2;
    var settings = 'dependent=yes,';
    settings +='directories=no,';
    settings +='location=no,';
    settings +='menubar=no,';
    settings +='status=no,';
    settings +='titlebar=no,';
    settings +='innerWidth=950,';
    settings +='height='+h+',';
    settings +='width='+w+',';
    settings +='top='+wint+',';
    settings +='left='+winl+',';
    settings +='scrollbars=yes,';
    settings +='resizable=yes';
    win=window.open(url, "OtherPicks", settings);
    if(parseInt(navigator.appVersion) >= 4){win.window.focus();}
}


function viewPlayerStats(url) {

    var w = 950;
    var h = 420;
    var winl = (screen.width-w)/2;
    var wint = (screen.height-h)/2;
    var settings = 'dependent=yes,';
    settings +='directories=no,';
    settings +='location=no,';
    settings +='menubar=no,';
    settings +='status=no,';
    settings +='titlebar=no,';
    settings +='innerWidth=950,';
    settings +='height='+h+',';
    settings +='width='+w+',';
    settings +='top='+wint+',';
    settings +='left='+winl+',';
    settings +='scrollbars=yes,';
    settings +='resizable=yes';
    win=window.open(url, "playerStats", settings);
    if(parseInt(navigator.appVersion) >= 4){win.window.focus();}
}

function viewTeamRoster(url) {
    var w = 900;
    var h = 450;
    var winl = (screen.width-w)/2;
    var wint = (screen.height-h)/2;
    var settings = 'dependent=yes,';
    settings +='directories=no,';
    settings +='location=no,';
    settings +='menubar=no,';
    settings +='status=no,';
    settings +='titlebar=no,';
    settings +='innerWidth=450,';
    settings +='height='+h+',';
    settings +='width='+w+',';
    settings +='top='+wint+',';
    settings +='left='+winl+',';
    settings +='scrollbars=yes,';
    settings +='resizable=yes';
    win=window.open(url, "teamRoster", settings);
    if(parseInt(navigator.appVersion) >= 4){win.window.focus();}
}

function viewTeamSchedule(url) {

    var w = 650;
    var h = 750;
    var winl = (screen.width-w)/2;
    var wint = (screen.height-h)/2;
    var settings = 'dependent=yes,';
    settings +='directories=no,';
    settings +='location=no,';
    settings +='menubar=no,';
    settings +='status=no,';
    settings +='titlebar=no,';
    settings +='innerWidth=650,';
    settings +='height='+h+',';
    settings +='width='+w+',';
    settings +='top='+wint+',';
    settings +='left='+winl+',';
    settings +='scrollbars=yes,';
    settings +='resizable=yes';
    win=window.open(url, "TeamSchedule", settings);
    if(parseInt(navigator.appVersion) >= 4){win.window.focus();}
}

function getElement(elemID) {
    return (document.getElementById) ? document.getElementById(elemID) :
               ((document.all) ? document.all[elemID] : null);
}

function createRequestObject() {
    var tmpXmlHttpObject;

    if (window.XMLHttpRequest) {
        tmpXmlHttpObject = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        tmpXmlHttpObject = new ActiveXObject("Microsoft.XMLHTTP");
    }

    return tmpXmlHttpObject;
}
