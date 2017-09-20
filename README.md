# deliverBot
deliverBot is Part of the graduation project, main goal to send some items and location witch user selected to specific server.
Server side implemented using PHP.

<table align="center">
  <tr>
        <td><p> Registration interface </p> <img width = 320 hight = 569 src="https://drive.google.com/uc?export=download&id=0Bxx_zwvRk-MbZ1JldE1ieGZHQlk"/></td>
        <td><p> Menu interface </p> <img src="https://drive.google.com/uc?export=download&id=0Bxx_zwvRk-MbbHNWa3FaWFVVUjA"/></td>
        <td><p> Location interface </p> <img src="https://drive.google.com/uc?export=download&id=0Bxx_zwvRk-MbV2pxQ2xFTnVmX2M"/></td>
        <td><p> Receipt interface </p> <img src="https://drive.google.com/uc?export=download&id=0Bxx_zwvRk-Mbb1pHT29scTZ6Q2c"/></td>
    </tr>
</table>

### Registration interface

create an account, and this activity will take you then to an activity to send four registration photo.

### Menu interface

content of expandable category menu of different types of snacks.
sample of sub lists with selectable items, and cart for the selected items.  


### Location interface

Map representation of selectable cells, wich is a recyclerview with grid layout manager.

### Receipt interface

Brief of all order info before requesting the order, give the user chance to finalizing the order.

# user experience

<table align="center">
    <tr>
        <td><p> Navegation drawer </p> <img src="https://drive.google.com/uc?export=download&id=0Bxx_zwvRk-Mbd0owdVdPV0pubXc"/></td>
        <td><p> Shared element transition </p> <img src="https://drive.google.com/uc?export=download&id=0Bxx_zwvRk-MbVGcyR3NZX09Iam8"/></td>
    </tr>
</table>


By using ```Navigation drawer``` we give the user a good hierarchic structure of interfaces.

and by using ```shared element transtion``` we implement a meaningful motion not just any animation.
By telling the user this element is the same in two activity not a new one and so on. 


## general things

all data (images and orders) transmitted through volley library.
Volley interduce by google I/O 2013  use http protocol to connect to the server.
Every response from the serever side use HTTP protocol wich encoded as JSON Array of JSON objects.

You can download a `.apk` from GitHub's [releases page](https://github.com/samerkador/deliverBot/releases).
``` DeliverBot ``` requires a minimum SDK version of 19 KITKAT.




