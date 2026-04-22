showAnimate = [
    {
        height: "0"
    }, {
        height: "100%"
    }
]

hideAnimate = [
    {
        height: "100%"
    }, {
        height: "0"
    }
]

animateOptions = {
    duration: 400,
    iterations: 1,
    fill: 'forwards'
}
var activeElement;

function setSelected(event) {
    if(activeElement){
        activeElement.animate(hideAnimate, animateOptions);
    }

    let overlays = event.getElementsByClassName("image-overlay");
    overlays[0].animate(showAnimate, animateOptions);
    activeElement = overlays[0];
}

function setUnselected() {
    activeElement.animate(hideAnimate, animateOptions);
}

const validStyles = [
    "default",
    "red",
    "blue",
    "green",
    "yellow",
    "orange",
    "purple",
    "pink",
    "dark-default",
    "dark-red",
    "dark-blue",
    "dark-green",
    "dark-yellow",
    "dark-orange",
    "dark-purple",
    "dark-pink",
]

const colorList = [
    {
      colorStyle: '#d1d1d1',
      styleName: 'default'
    },{
      colorStyle: '#ff0000ff',
      styleName: 'red'
    },{
      colorStyle: 'rgb(0, 171, 255)',
      styleName: 'blue'
    },{
      colorStyle: 'rgb(8, 223, 41)',
      styleName: 'green'
    },{
      colorStyle: 'rgb(255, 239, 1)',
      styleName: 'yellow'
    },{
      colorStyle: 'rgb(255, 120, 1)',
      styleName: 'orange'
    },{
      colorStyle: 'rgb(221, 99, 255)',
      styleName: 'purple'
    },{
      colorStyle: 'rgb(255, 59, 243)',
      styleName: 'pink'
    }
  ]

// Basic Element Containers and Items
const elementContainers = document.getElementsByClassName('element-container');
const elementItems = document.getElementsByClassName('element-item');

// Popup Mterials
const popupElement = document.getElementById("color-popup");
const popupCloseButton = document.getElementById("close-popup");
const colorPanel = document.getElementById("color-panel");
const actSearch = document.getElementById("act-search");
const colorStyleCheck = document.getElementById("color-style-check");
const updateStyleButton = document.getElementById("update-style-button");

function updateStyleColor(newStyle) {
    if(style.startsWith("dark-")) {
        newStyle = `dark-${newStyle}`;
    }

    removeCurrentStyle();
    style = newStyle;
    addCurrentStyle();
    updateStyleButton.disabled = (style == persistedStyle);
}

function updateStyleDarkness(useDark) {
    let newStyle = style;
    if(useDark){
        // Setting to dark mode
        if(newStyle.startsWith("dark-")) return ; // nothing to do
        newStyle = `dark-${newStyle}`;
    } else {
        // Setting to light mode
        if(!newStyle.startsWith("dark-")) return ; // nothing to do
        newStyle = newStyle.substring(5);
    }

    removeCurrentStyle();
    style = newStyle;
    addCurrentStyle();
    updateStyleButton.disabled = (style == persistedStyle);
}

function persistStyle(){
    localStorage.setItem("tc_style_full", style);
    persistedStyle = style;
    updateStyleButton.disabled = true;
}

function addCurrentStyle() {
    for(let c = 0; c < elementContainers.length; c++){
        let element = elementContainers.item(c);
        if(!element) continue;
        element.classList.add(`element-container-${style}`);
    }
    for(let c = 0; c < elementItems.length; c++){
        let element = elementItems.item(c);
        if(!element) continue;
        element.classList.add(`element-item-${style}`);
    }

    // Set the popup elements
    popupElement.classList.add(`popup-background-${style}`);
}

function removeCurrentStyle(){
    for(let c = 0; c < elementContainers.length; c++){
        let element = elementContainers.item(c);
        if(!element) continue;
        element.classList.remove(`element-container-${style}`);
    }
    for(let c = 0; c < elementItems.length; c++){
        let element = elementItems.item(c);
        if(!element) continue;
        element.classList.remove(`element-item-${style}`);
    }

    // Set the popup elements
    popupElement.classList.remove(`popup-background-${style}`);
}

let style = "";
let persistedStyle = "";

document.addEventListener("DOMContentLoaded", () => {
    style = localStorage.getItem("tc_style_full");

    if(!style || !validStyles.includes(style)){
        style = validStyles[0];
    } else {
        persistedStyle = style;
    }

    addCurrentStyle();
    // Set pupup elements
    popupElement.hidden = true;

    popupCloseButton.addEventListener("click", () => onPopupShow(false));
    actSearch.addEventListener("click", ()=> onPopupShow(true));
    updateStyleButton.addEventListener("click", ()=> persistStyle());
    colorStyleCheck.addEventListener("change", (ev) => {
        updateStyleDarkness(ev.target.checked);
    })
    colorStyleCheck.checked = style.startsWith("dark-");


    for(let possibleStyle of colorList){
        let newColorPallet = document.createElement("div");
        newColorPallet.classList.add('color-button');
        newColorPallet.style.backgroundColor = possibleStyle.colorStyle;

        newColorPallet.addEventListener("click", () => {
            updateStyleColor(possibleStyle.styleName);
        });

        colorPanel.appendChild(newColorPallet);
    }
})

function onPopupShow(doShow) {
    popupElement.hidden = !doShow;
    if(doShow) {
        updateStyleButton.disabled = (style == persistedStyle);
    }
}