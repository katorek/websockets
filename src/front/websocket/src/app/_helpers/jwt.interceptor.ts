import {Injectable} from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthenticationService} from '../_services/authentication.service';

// import { AuthenticationService } from '@/_services';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor(private authenticationService: AuthenticationService) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add authorization header with jwt token if available
        // const currentUser = this.authenticationService.currentUserValue;
        // if (currentUser && currentUser.basicAuth) {
        //     request = request.clone({
        //         setHeaders: {
        //             Authorization: `Basic ${currentUser.basicAuth}`
        //         }
        //     });
        // }

        return next.handle(request);
    }

    // intercept2(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    //     // add authorization header with jwt token if available
    //     const currentUser = this.authenticationService.currentUserValue;
    //     if (currentUser && currentUser.token) {
    //         request = request.clone({
    //             setHeaders: {
    //                 Authorization: `Basic ${currentUser.token}`
    //             }
    //         });
    //     }
    //
    //     return next.handle(request);
    // }
}
