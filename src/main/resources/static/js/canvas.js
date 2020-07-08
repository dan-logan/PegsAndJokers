var canvas = document.querySelector('canvas');
console.log("canvas: " + canvas.width+","+canvas.height);
console.log("window: " + window.innerWidth+","+window.innerHeight);

if (window.innerHeight < window.innerWidth)
  {
    canvas.width = window.innerHeight*0.75;
    canvas.height = window.innerHeight*0.75;
  }
  else{
    canvas.width = window.innerWidth*0.75;
    canvas.height = window.innerWidth*0.75;
  }

var c = canvas.getContext('2d');


var scaleToggle =false;
var zoom = {x: 0, y:0};

var mc = new Hammer.Manager(canvas);

mc.add(new Hammer.Tap({event: 'doubletap', taps: 2}));

mc.on("doubletap", function(ev) {
  zoom.x = ev.center.x;
  zoom.y = ev.center.y;
  scaleToggle = !scaleToggle;
  init();
});


 window.addEventListener('resize', function (event) {
  if (window.innerHeight < window.innerWidth)
  {
    canvas.width = window.innerHeight*0.75;
    canvas.height = window.innerHeight*0.75;
  }
  else{
    canvas.width = window.innerWidth*0.75;
    canvas.height = window.innerWidth*0.75;
  }  

  init();

 })

  class BurnCard {

    constructor(x,y) {

      this.x =x;
      this.y=y;
      this.width = 3*BOARD.holeRadius;
      this.height = 1.5*this.width;

        this.draw = function() {
        c.save();
        c.fillStyle = "Blue"
        c.shadowColor + "Black";
        c.fillRect(this.x, this.y, this.width, this.height);
        c.rect(this.x, this.y, this.width, this.height);
        c.strokeStyle = "White";
        c.lineWidth = this.width/10;
        c.stroke()
        c.restore();
      } 
    }

 }

 class PegHole {

    constructor(x, y, hasPeg, pegColor, pegNumber,rotateText) {
      this.x = x;
      this.y = y;
      this.pegColor = pegColor;
      this.hasPeg = hasPeg;
      this.radius = BOARD.holeRadius;
      this.pegNumber = pegNumber;

      this.draw = function(){
          c.beginPath();
          c.arc(this.x, this.y, this.radius, 0, Math.PI*2);
          if(hasPeg)
          {
            c.fillStyle = this.pegColor;
            c.fill();
            c.save();
            let textAngle =0;
            if (rotateText == true)
            { 
              textAngle=Math.PI;
            }

            c.translate(this.x,this.y);
            c.rotate(textAngle);
            c.translate(-this.x,-this.y);

            c.font = this.radius*1.5+ "px Arial";
            c.textAlign = "center";
            c.textBaseline = "middle";
            c.fillStyle = "Black";
            c.fillText(this.pegNumber,this.x,this.y);
            c.restore();
          }
          else
          {
            c.fillStyle = "Black";
            c.fill();
          }
          c.lineWidth = this.radius*0.2;
          c.stroke();
  
      }
      
    }
  }

 function autosize() {
	 document.cookie = "sizePreference=auto";
	 init();
 }
 
 function largeSize() {
	 document.cookie = "sizePreference=large";
	 init();
 }
 
 function extraLargeSize() {

	 document.cookie = "sizePreference=xl";
	 init();
	 
 }
 
 function getCookie(cname) {
	  var name = cname + "=";
	  var decodedCookie = decodeURIComponent(document.cookie);
	  var ca = decodedCookie.split(';');
	  for(var i = 0; i <ca.length; i++) {
	    var c = ca[i];
	    while (c.charAt(0) == ' ') {
	      c = c.substring(1);
	    }
	    if (c.indexOf(name) == 0) {
	      return c.substring(name.length, c.length);
	    }
	  }
	  return "";
}
 
function setCanvasSize()
{
	var size = getCookie("sizePreference")


	if(!size && size=="auto")
		{
		 if (window.innerHeight < window.innerWidth)
		  {
		    canvas.width = window.innerHeight*0.75;
		    canvas.height = window.innerHeight*0.75;
		  }
		  else{
		    canvas.width = window.innerWidth*0.75;
		    canvas.height = window.innerWidth*0.75;
		  }

		}
	else if (size == "large")
		{
		 canvas.width = 768;
		 canvas.height = 768;
		}
	else if (size == "xl")
		{
		 canvas.width = 1024;
		 canvas.height = 1024;
		}
	
}
function init( )
{
	if(scaleToggle)
	  {
	    c.translate(zoom.x,zoom.y);
	    c.scale(2,2);
	    c.translate(-zoom.x,-zoom.y);
	  }
	  else
	  {
	    //c.scale(1,1);
	    c.setTransform(1,0,0,1,0,0);
	  }
	   
	var numberOfSides = playerView.board.playerSides.length;
  
  setCanvasSize();
  
  this.BOARD = {
    centerX: canvas.width/2,
    centerY: canvas.height/2,
    radius: 0.95*canvas.height/2,
    apothem: 0.95*(canvas.height/2) * Math.cos(Math.PI/numberOfSides),
    numberOfSides: numberOfSides,
    sideLength: 2 * 0.95* (canvas.height/2) * Math.sin(Math.PI/numberOfSides),
    sideWidth: canvas.height * 0.01,
    holeRadius: (2 * 0.95* (canvas.height/2) * Math.sin(Math.PI/numberOfSides))/45
  }
  
   c.clearRect(0,0,canvas.width, canvas.height);
   c.fillStyle = "papayawhip"
   c.fillRect(0,0,canvas.width, canvas.height);
   drawBoard();
   c.setTransform(1,0,0,1,0,0);
}

function drawBurnCards(numberBurned, dx)
{
  
  c.fillStyle = "Grey";
  c.strokeStyle = "Black";
  let burnRectX = BOARD.centerX-BOARD.holeRadius;
  let burnRectY =BOARD.centerY+BOARD.apothem+1.5*BOARD.holeRadius
  let burnRectWidth = 8.5*BOARD.holeRadius;
  let burnRectHeight = 0.75*burnRectWidth;

  c.save();
  let rotateAngle = 0;
  if (dx == -1)
  {
    rotateAngle = Math.PI;
  }

  c.translate((burnRectX+BOARD.holeRadius),(burnRectY-1.5*BOARD.holeRadius));
  c.rotate(rotateAngle);
  c.translate(-(burnRectX+BOARD.holeRadius),-(burnRectY-1.5*BOARD.holeRadius));

  c.fillRect(burnRectX,burnRectY,burnRectWidth, burnRectHeight);
 
  c.fillStyle = "White";
  c.textBaseline = "top";
  c.font = BOARD.holeRadius+"px Arial";
  c.fillText("Burned Cards", burnRectX, burnRectY);
  
  if (numberBurned > 0)
  {
    let cardX = burnRectX + BOARD.holeRadius;
    let cardY = burnRectY + 1.2*BOARD.holeRadius;

    burnCard = new BurnCard(cardX,cardY);
    burnCard.draw();

    if (numberBurned > 1)
    {
      burnCard = new BurnCard(cardX+4*BOARD.holeRadius,cardY);
      burnCard.draw();
    }
  }
  c.restore();
  
}


function drawBoard()
{
  c.save();
  n=BOARD.numberOfSides;  //number of sides
 
  r = BOARD.radius;
  a = BOARD.apothem; //apothem
  l = BOARD.sideLength; //side length
  x = BOARD.centerX;
  y = BOARD.centerY;
 
  drawSideFromCenter(x,y,a,l,1); //side 1 track

  //loop thru remaining sides up to side n to draw track
  for (let s = 2; s <= n; s++) {
   
    if (s%2 == 0) 
    {
      dx = -1;
    }
    else
    {
      dx = +1
    }
    c.translate(x+(dx*l)/2,y+a);
    c.rotate(Math.PI*2-(180 - (360/n))*Math.PI/180);
    c.translate(-x-(dx*l)/2, -y-a);
    drawSideFromCenter(x,y,a,l,dx);
 
  }

  c.restore();
  //c.setTransform(1,0,0,1,0,0);

  //rotate to show current player on bottom
  bottomSide = playerView.playerNumber;
  c.translate(BOARD.centerX,BOARD.centerY);
  c.rotate(-Math.PI*2*(bottomSide-1)/n);
  c.translate(-BOARD.centerX,-BOARD.centerY);
  
  //draw first side
  drawHolesFromCenter(x,y,a,l,1,playerView.board.playerSides[0]); //holes for side 1
  drawBurnCards(playerView.burntCardCounts[0],1);
  //loop thru remaining sides up to side n to draw holes
  for (let s = 2; s <= n; s++) {
   
    if (s%2 == 0) 
    {
      dx = -1;
    }
    else
    {
      dx = +1
    }
    c.translate(x+(dx*l)/2,y+a);
    c.rotate(Math.PI*2-(180 - (360/n))*Math.PI/180);
    c.translate(-x-(dx*l)/2, -y-a);
    drawHolesFromCenter(x,y,a,l,dx,playerView.board.playerSides[s-1]);
    drawBurnCards(playerView.burntCardCounts[s-1],dx);

  }


}

init();


function drawSideFromCenter(x, y, a, l,dx)
{
  c.strokeStyle = "black";
  //using x, y as cooridinates of ceter draw line of length l
  c.font = "30px Arial";
  c.beginPath();
  c.moveTo(x+l/2,y+a);
  c.lineTo(x-l/2,y+a);
  c.lineWidth=BOARD.sideWidth;
  c.stroke();

  let holeSpacing = dx*BOARD.holeRadius*2.5;

  //draw home track
  let pegx = x+(dx*l/2) - 3*holeSpacing;
  c.moveTo(pegx,y+a);
  c.lineTo(pegx, y+a-3*holeSpacing);
  c.stroke();
  c.lineTo(pegx - 2*holeSpacing, y+a-3*holeSpacing);
  c.stroke();

  //draw start position track
  pegx = x+(dx*l/2) - 8*holeSpacing;
  c.moveTo(pegx,y+a);
  c.lineTo(pegx,y+a-3*holeSpacing);
  c.stroke();
  c.moveTo(pegx-holeSpacing, y+a-2*holeSpacing);
  c.lineTo(pegx+holeSpacing, y+a-2*holeSpacing);
  c.stroke();
}

function drawHolesFromCenter(x, y, a, l,dx,side)
{
  let holeSpacing = dx*BOARD.holeRadius*2.5;
  //rotate text on even number sides
  let rotateText = false;
  if (dx == -1)  
  {
    rotateText=true;
  }
  
  //add main track holes to side
  pegx = x+(dx*l/2) - holeSpacing;
  side.mainTrackPositions.forEach(pos => {

    c.lineWidth = 1;
    pegHole = new PegHole(pegx,y+a,pos.hasPeg, pos.pegColor,pos.pegNumber,rotateText);
    pegHole.draw();
    pegx = pegx - holeSpacing;
  
  });

  //add start position holes
   pegx = pegx + 11*holeSpacing;
   let startHole = side.startPositions[0];
   pegHole = new PegHole(pegx,y+a-holeSpacing, startHole.hasPeg,startHole.pegColor,startHole.pegNumber,rotateText);
   pegHole.draw();
   startHole = side.startPositions[1];
   pegHole = new PegHole(pegx,y+a-2*holeSpacing, startHole.hasPeg,startHole.pegColor,startHole.pegNumber,rotateText);
   pegHole.draw();
   startHole = side.startPositions[2]
   pegHole = new PegHole(pegx,y+a-3*holeSpacing, startHole.hasPeg,startHole.pegColor,startHole.pegNumber,rotateText);
   pegHole.draw();
   startHole = side.startPositions[3];
   pegHole = new PegHole(pegx-holeSpacing,y+a-2*holeSpacing,startHole.hasPeg,startHole.pegColor,startHole.pegNumber,rotateText);
   pegHole.draw();
   startHole = side.startPositions[4];
   pegHole = new PegHole(pegx+holeSpacing,y+a-2*holeSpacing, startHole.hasPeg,startHole.pegColor,startHole.pegNumber,rotateText);
   pegHole.draw();

   //add home positionholes
   pegx = pegx + 5*holeSpacing;
   let homeHole = side.homePositions[0];
   pegHole = new PegHole(pegx,y+a-holeSpacing, homeHole.hasPeg,homeHole.pegColor,homeHole.pegNumber,rotateText);
   pegHole.draw();
   homeHole = side.homePositions[1];
   pegHole = new PegHole(pegx,y+a-2*holeSpacing, homeHole.hasPeg,homeHole.pegColor,homeHole.pegNumber,rotateText);
   pegHole.draw();
   homeHole = side.homePositions[2];
   pegHole = new PegHole(pegx,y+a-3*holeSpacing,homeHole.hasPeg,homeHole.pegColor,homeHole.pegNumber,rotateText);
   pegHole.draw();
   homeHole = side.homePositions[3];
   pegHole = new PegHole(pegx-holeSpacing,y+a-3*holeSpacing,homeHole.hasPeg,homeHole.pegColor,homeHole.pegNumber,rotateText);
   pegHole.draw();
   homeHole = side.homePositions[4];
   pegHole = new PegHole(pegx-2*holeSpacing,y+a-3*holeSpacing,homeHole.hasPeg,homeHole.pegColor,homeHole.pegNumber,rotateText);
   pegHole.draw();
  
}