import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {User} from '../_models/user';
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {
    private currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>;
    authorities: Array<string>;

    private register = environment.register;

    constructor(private http: HttpClient) {
        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserValue(): User {
        return this.currentUserSubject.value;
    }

    attemptAuth(username: string, password: string): Observable<any> {
        const credentials = {username: username, password: password};
        return this.http.post(this.register, credentials);
    }

    login(username: string, password: string) {
        console.log(username);
        // return this.http.post<any>(this.register, {username, password})
        //     .pipe(map(user => {
        //         // login successful if there's a jwt token in the response
        //         if (user && user.token) {
        //             this.authorities = user.authorities;
        //             // store user details and jwt token in local storage to keep user logged in between page refreshes
        //             localStorage.setItem('currentUser', JSON.stringify(user));
        //             this.currentUserSubject.next(user);
        //         }
        //
        //         return user;
        //     }));
        return this.http.post<any>(this.register, {'name': username})
            .pipe(map(data => {
                console.log(data);
                localStorage.setItem('currentUser', JSON.stringify({'name': username}));

                // login successful if there's a jwt token in the response
                // if (user && user.token) {
                //     this.authorities = user.authorities;
                //     // store user details and jwt token in local storage to keep user logged in between page refreshes
                //     localStorage.setItem('currentUser', JSON.stringify(user));
                //     this.currentUserSubject.next(user);
                // }
                //
                // return user;
            }));
    }

    public hasRole(role: string): boolean {
        if (this.currentUserValue) {
            const authorities = this.currentUserValue.authorities;
            return authorities && authorities.includes(role);
        }
        return false;
    }

    public currentUserUsername(): string {
        if (this.currentUserValue) {
            return this.currentUserValue.username;
        }
        return '';
    }

    public isAuthenticated(): boolean {
        const token = localStorage.getItem('currentUser');
        return token !== null;
    }

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
    }

    getInfoAboutYourself() {
        return this.http.get<User>(`${environment.api}/users/me`);
    }

    public isAdmin() {
        return this.hasRole('ROLE_ADMIN');
    }

}
