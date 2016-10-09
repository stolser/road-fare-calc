# road-fare-calc
The calculator of the total amount of fare for each user.
The project consists of two parts the Server and the Client.
The Client emulates an autobahn that consists of TrafficPosts connected with Roads. Car objects emulate the activity of users that can arrive at a TrafficPost, choose the next Road, hit this Road or leave the autobahn. 
All Roads are paid with a specific amount of fare. 
Registered users drive along the autobahn from one TrafficPost to another. Each time a user arrives at a TrafficPost or hits the next Road this TrafficPost sends via sockets a message to the Server with the following info: trafficPostId, userId, status (AT_TRAFFIC_POST / ON_THE_ROAD / LEFT_AUTOBAHN), roadId and time. 
The Server tracks all active users being driving on the autobahn and save the information about their movements in the DB. 
When a user leaves the autobahn the Server calculates the total amount of fare (a sum of the fare for each Road that was used by this User) and sends an email to the User with this amount and the list of all Roads that the User used during his journey. 

Technologies: MongoDB, Spring IoC, Spring Data, javax.mail. 


