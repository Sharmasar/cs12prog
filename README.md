# cs12prog

A user-friendly program called MediaTracker was created to assist users in keeping track of the entertainment material they read, watch, and play, including video games, TV series, movies, and books. The application enables users to make a personal watchlist or readlist, mark their accomplishments, and monitor their progress. Simple coding tools appropriate for 11th graders are used in its construction, along with file storage and straightforward menus in a terminal or graphical user interface.
Users are shown a login screen when the software launches. They can then choose to establish a new account or log in using an already-existing one. A simple structure, such as username and password, is used to store account information in a plain text file. The application determines whether the username is already in use when a user signs up. The new account is added to the file if it is accessible. The application validates the credentials saved in the file with the ones entered during the login process. The user is given access to the main dashboard if the data matches. Otherwise, an error notice appears.
Once logged in, the user sees the main dashboard with a menu that provides access to four key functions:
Add Media


View Library


Edit or Delete Media


Watchlist/Readlist


In the "Add Media" section, users can create a new media entry by filling out a simple form. They will enter:
The title (e.g., The Hunger Games)


The type of media (TV, Movie, Book, Game)


The status (Completed, Watching, Plan to Watch, Paused)


A    rating (from 1 to 5)


Notes (an optional short description)


Each entry is saved to a text file in the format:
 Username	|title|	type	|status|	       rating	|notes.
 This system ensures that media entries are linked to the correct user and easily readable by the program.
The “View Library” option allows users to see a list of all the media they’ve added. The program searches the media file for all entries that match the logged-in user and displays them in a clean, readable layout. For example:
Title: The Hunger Games  
Type: Book  
Status: Completed  
Rating: 5  
Notes: Great story

The "Edit or Delete Media" function lets users find a media entry they previously added and update the rating, status, or notes. They can also delete the entry entirely. This is done by reading the file line by line, identifying the correct entry using the username and title, and then updating or removing that line.
The “Watchlist/Readlist” section is automatically populated whenever a user adds a media item with the status “Plan to Watch.” This list allows users to quickly see what they’ve saved to view or read later. They can then update the status to “Watching” or “Completed” from the Edit section when they begin or finish the media.
Optionally, students can add bonus features to improve their program and potentially earn extra marks. Some ideas include:
A favorite toggle that adds a to favorite media


Sorting the library by rating or status


Displaying the total number of media items saved


Simple achievements like “Added 5 entries”


Finally, the application includes a logout option that returns the user to the login screen. The program keeps track of the current user during the session using a variable like currentUser. 




Before the user may start entering media, they must first sign up. A secure password and a unique username are required for the sign-up form. A minimum of eight characters and a maximum of twenty characters are required for both the username and password. The user will get a clear error message and be unable to proceed if either field is left empty or does not reach the required length. For security purposes, passwords are hashed, and each user's username must be distinct. A user will be prompted to select a different username if they try to register using one that has already been taken. The user is automatically logged in after the account has been successfully created. Users have to log in again after logging out. If incorrect login credentials are entered, the user will be notified whether the error was with the username or password. Since email addresses are not used, there is no way to recover lost accounts or reset forgotten passwords from the client side.

The user is taken to the main dashboard after logging in. The user can enter new media in the input part at the top of the screen, which also shows the number of media items that are currently saved in their profile. All of the main areas of the application, including the notebook view, favorites, watch/read list, achievement statistics, user manual, and logout button, are easily accessible through a sidebar that can be folded up.

The Media Input Section is the core functionality of the MediaTracker application, designed to provide users with an intuitive and efficient way to log and categorize their media consumption habits. This feature is accessible from the main dashboard and occupies a dedicated space at the top of the screen for quick access and consistent visibility throughout the session. It is built using a series of well-labeled text fields, dropdown menus, toggle switches, and buttons that guide the user step-by-step through the entry process, minimizing input errors and maximizing clarity.
Upon accessing this section, the user is first prompted to select the Media Type from a dropdown menu, which includes primary categories such as:
Book


Movie


TV Show


Video Game


After selecting the media type, a dynamic Subcategory Menu appears based on the type chosen. For example:
Books can be subcategorized as Novel, Graphic Novel, Fiction, Non-fiction, Fiction


Movies can include
1. Documentary
2. Horror
3. Action
4. Comedy
5. Romcom
6. Romance
7. Mystery
8. Thriller
9. Dystopian
10. Drama



TV Shows include 
1. Documentary
2. Horror
3. Action
4. Comedy
5. Romcom
6. Romance
7. Mystery
8. Thriller
9. Dystopian
10. Drama



Video Games include RPG, MULTIPLAYER, SINGLEPLAYER (if MULTIPLAYER or SINGLEPLAYER, ask user if the game is a battle royale game).
(Then asked what genre)
1. Documentary
2. Horror
3. Action
4. Comedy
5. Romcom
6. Romance
7. Mystery
8. Thriller
9. Dystopian
10. Drama



This modular structure ensures that every entry is classified with precision, making future sorting, searching, and filtering more effective.
Once the category and subcategory are selected, the user proceeds to enter the Media Title, a required text field that accepts alphabetic and special characters (with validation to prevent blank or improperly formatted titles). Next, the user provides the Creator information — for instance, the name of the author, director, studio, or developer depending on the media type.
The Genre field allows users to further classify their media entry. This can be a free-entry field or a selectable list (with options like Fantasy, Action, Romance, Sci-Fi, Thriller, etc.), and users can input multiple genres using comma-separated values for hybrid works.
Users then assign a Numeric Rating between 1 and 10 using a slider or input box. This rating not only influences the color-coded visual scheme of the media item in the library view (as defined in the UI section) but also feeds into overall statistics and achievement progress. Ratings are validated to accept only whole numbers within the designated range.
Following the rating, the user can write a Short Review or Personal Notes (up to a recommended 250 characters) summarizing their impressions, thoughts, or observations. This review field accepts multiple lines and supports basic formatting for readability.
Next, the user selects the Current Status of the media item from a dropdown list that includes:
Plan to Watch/Read/Play


In Progress


Paused


Completed


Dropped


This status determines where the item appears in the broader application interface — for example, media marked as “Plan to Watch/Read” is automatically added to the user’s Watch/Read Queue, and “Completed” media may trigger achievements or affect recommendation logic in future expansions.
After all required fields are filled in, the user can click the Submit Button (or press Enter if using keyboard navigation). The program performs a final validation check to ensure all required fields are complete and properly formatted. If any validation fails (e.g., a rating outside 1–10, or a missing title), a detailed error message appears beneath the input fields.
To prevent duplication and ensure data integrity, the program checks the existing media database for any previously entered item that matches the current user's username and the same title + media type combination. If a duplicate is detected, a warning message appears in red text:
“This media item already exists in your collection. Please update the existing entry or choose a different title.”
If the entry is unique, the program writes the new media item to the file in a structured format (e.g., a tab-separated or JSON entry), including all user-specific metadata. Upon successful entry, a green confirmation message is displayed, summarizing the saved media information:
“Saved! You’ve added The Hunger Games [Book – Novel] to your library.”
Additionally, the input fields are automatically cleared, allowing users to quickly log multiple entries without manual reset, streamlining the experience for bulk additions (e.g., when a user is importing their entire media backlog).
The section also includes optional helper text or tooltips next to each input field, explaining its purpose, e.g., “Use this field to summarize your thoughts,” or “Select a number from 1–10 based on your personal experience.”
