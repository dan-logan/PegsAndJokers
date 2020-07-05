var canvas = document.querySelector('canvas');

if (window.innerHeight < window.innerWidth)
{
  canvas.width = window.innerHeight;
  canvas.height = window.innerHeight;
}
else{
  canvas.width = window.innerwidth;
  canvas.height = window.innerwidth;
}  


var c = canvas.getContext('2d');


 window.addEventListener('resize', function (event) {
  if (window.innerHeight < window.innerWidth)
  {
    canvas.width = window.innerHeight;
    canvas.height = window.innerHeight;
  }
  else{
    canvas.width = window.innerWidth;
    canvas.height = window.innerWidth;
  }  

  init();

 })

 class PegHole {

    constructor(x, y, hasPeg, pegColor, pegNumber) {
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
            c.font = this.radius*1.5+ "px Arial";
            c.textAlign = "center";
            c.textBaseline = "middle";
            c.fillStyle = "Black";
            c.fillText(this.pegNumber,this.x,this.y);
          }
          else
          {
            c.fillStyle = "Black";
            c.fill();
          }
          c.lineWidth = 5;
          c.stroke();
  
      }
      
    }
  }
  
function init( )
{
  var numberOfSides = playerView.board.playerSides.length;
  
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


function drawBoard()
{
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

  c.setTransform(1,0,0,1,0,0);
  drawHolesFromCenter(x,y,a,l,1,playerView.board.playerSides[0]); //holes for side 1
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

  }


}

init();

function drawSideFromCenter(x, y, a, l,dx)
{
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
  
   //add main track holes to side
  pegx = x+(dx*l/2) - holeSpacing;
  side.mainTrackPositions.forEach(pos => {

    c.lineWidth = 1;
    pegHole = new PegHole(pegx,y+a,pos.hasPeg, pos.pegColor,pos.pegNumber);
    pegHole.draw();
    pegx = pegx - holeSpacing;
  
  });

  //add start position holes
   pegx = pegx + 11*holeSpacing;
   let startHole = side.startPositions[0];
   pegHole = new PegHole(pegx,y+a-holeSpacing, startHole.hasPeg,startHole.pegColor,startHole.pegNumber);
   pegHole.draw();
   startHole = side.startPositions[1];
   pegHole = new PegHole(pegx,y+a-2*holeSpacing, startHole.hasPeg,startHole.pegColor,startHole.pegNumber);
   pegHole.draw();
   startHole = side.startPositions[2]
   pegHole = new PegHole(pegx,y+a-3*holeSpacing, startHole.hasPeg,startHole.pegColor,startHole.pegNumber);
   pegHole.draw();
   startHole = side.startPositions[3];
   pegHole = new PegHole(pegx-holeSpacing,y+a-2*holeSpacing,startHole.hasPeg,startHole.pegColor,startHole.pegNumber);
   pegHole.draw();
   startHole = side.startPositions[4];
   pegHole = new PegHole(pegx+holeSpacing,y+a-2*holeSpacing, startHole.hasPeg,startHole.pegColor,startHole.pegNumber);
   pegHole.draw();

   //add home positionholes
   pegx = pegx + 5*holeSpacing;
   let homeHole = side.homePositions[0];
   pegHole = new PegHole(pegx,y+a-holeSpacing, homeHole.hasPeg,homeHole.pegColor,homeHole.pegNumber);
   pegHole.draw();
   homeHole = side.homePositions[1];
   pegHole = new PegHole(pegx,y+a-2*holeSpacing, homeHole.hasPeg,homeHole.pegColor,homeHole.pegNumber);
   pegHole.draw();
   homeHole = side.homePositions[2];
   pegHole = new PegHole(pegx,y+a-3*holeSpacing,homeHole.hasPeg,homeHole.pegColor,homeHole.pegNumber);
   pegHole.draw();
   homeHole = side.homePositions[3];
   pegHole = new PegHole(pegx-holeSpacing,y+a-3*holeSpacing,homeHole.hasPeg,homeHole.pegColor,homeHole.pegNumber);
   pegHole.draw();
   homeHole = side.homePositions[4];
   pegHole = new PegHole(pegx-2*holeSpacing,y+a-3*holeSpacing,homeHole.hasPeg,homeHole.pegColor,homeHole.pegNumber);
   pegHole.draw();
  
}