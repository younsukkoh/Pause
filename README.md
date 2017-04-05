# Pause
### Created by Younsuk Koh ([Github](https://github.com/younsukkoh), [LinkedIn](https://www.linkedin.com/in/younsukkoh/))
___

### Description:
Social networking Android mobile application. Enables users to upload photos to cloud storage to save memory on their devices; Search for other friends and friend them; Share photos with other users by creating a virtual room. 
___

### Sign In & Sign Up: 
User information will be added to Firebase Database upon Sign Up. User may Sign In as soon as he/she finishes Signing Up.

<table class="image">
  <tr>
    <th>Sign In Screen</th> <th>Sign Up (a)</th> <th>Sign Up (b)</th>
  </tr>
  <tr>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685727/6509ae4a-197d-11e7-94ae-e18bf3a7d512.png" width="200" height="350">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685726/65092998-197d-11e7-944b-57edf793c5f3.png" width="200" height="350">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685728/650ac2c6-197d-11e7-92de-c8707e9e97b7.png" width="200" height="350">
    </td>
  </tr>
</table>

### Personal Memory: 
The contents in this section can only be accessed and viewed by the user him/herself.
New Memory (the block with pictures) is created when the user takes a photo. 
Then on top of the existing Memory, the user add more pictures. 
Memory can be edited to hold more information.

<table class="image">
  <tr>
    <th>Personal Memories</th> <th>Adding A Memory</th> <th>Camera Interface</th> <th>Edit Memory Information</th>
  </tr>
  <tr>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685769/8c6ba43e-197d-11e7-8a23-2b05be6a5c8f.png" width="200" height="300">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685771/8c6de33e-197d-11e7-9ad7-c57fc01a5484.png" width="200" height="300">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24688886/1a7f4358-1991-11e7-9dd1-ed19dcbe2315.png" width="200" height="300">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685767/8c6abc68-197d-11e7-82ce-bbc701f210f6.png" width="200" height="300">
    </td>
  </tr>
</table>

Information about Memory (the user who took the picture, when and where the picture was taken) is saved in Firebase Database. 
Pictures are uploaded into Firebase Storage.

<table class="image">
  <tr>
    <th>Firebase Database</th> <th>Firebase Storage</th>
  </tr>
  <tr>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24688852/e08f7e2e-1990-11e7-91b9-ac57645c2292.png" width="350" height="150">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24689711/6c765a5c-1996-11e7-923f-8e276a49f285.png" width="350" height="150">
    </td>
  </tr>
</table>

### Individual Memory: 
Each Memory can hold multiple pictures. Each picture can be viewed separately.

<table class="image">
  <tr>
    <th>Individual Memory Contents</th> <th>View Image</th>
  </tr>
  <tr>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685765/8c5af774-197d-11e7-9465-d8b62a0e1835.png" width="200" height="350">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685766/8c646750-197d-11e7-8f10-29ca5300ae1b.png" width="200" height="350">
    </td>
  </tr>
</table>

### Becoming Friends With Other Users: 
A user can search for other users using their email addresses. 
When the email address matches one of the users, the searched user's profile will be displayed.
The user can choose to add the searched user as a Friend.

<table class="image">
  <tr>
    <th>Search for Other User</th> <th>Searched User Profile</th> <th>User Added as Friend</th> <th>User Friended</th>
  </tr>
  <tr>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685781/a139401a-197d-11e7-89be-f1e945a98840.png" width="200" height="300">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685780/a1382374-197d-11e7-8a0b-334c09c41a99.png" width="200" height="300">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685782/a13b4e46-197d-11e7-97e6-718ca0d8089c.png" width="200" height="300">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685779/a137d806-197d-11e7-9b3b-001643aa4db1.png" width="200" height="300">
    </td>
  </tr>
</table>

### Sharing is Caring: 
Once the user made some friends, the user can now create a virtual Room. 
When a new Memory is created in a Room, all the members of Room can see the contents.
All users can contribute to building a Memory by adding pictures.

<table class="image">
  <tr>
    <th>List of Friends</th> <th>Invite Friends To A Room</th> <th>Shared Memories</th>
  </tr>
  <tr>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685772/8c71ab90-197d-11e7-8502-0814c71ca086.png" width="200" height="350">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685804/ccc425e2-197d-11e7-9dac-46d160e2b645.png" width="200" height="350">
    </td>
    <td>
      <img src="https://cloud.githubusercontent.com/assets/8348540/24685805/ccc61bf4-197d-11e7-9016-8688d583a9c5.png" width="200" height="350">
    </td>
  </tr>
</table>

___
### Future Updates:
Better camera interface will be implemented. Faster upload to cloud storage will be implemented.
