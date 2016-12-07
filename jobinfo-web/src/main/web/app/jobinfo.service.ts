import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { JobInfo } from './jobinfo';
import { Job } from './job';

@Injectable()
export class JobInfoService {
    private baseURL = "http://localhost:4204/jobinfo-rest/jobinfo/rest";

    constructor( private http: Http ) {
    }

    getJobInfo( username: string, password: string, id: string, system: string ): Promise<JobInfo> {
        let url = this.baseURL + `?id=${id}&system=${system}`;

        let headers = ( username && password ) ? new Headers( { 'Authorization': 'Basic ' + btoa( username + ":" + password ) }) : null;
        let options = headers ? new RequestOptions( { headers: headers }) : null;
        return this.http.get( url, options ).toPromise().then( response => response.json() ) as Promise<JobInfo>;
    }

    private extract( resp: Response ) {
        return resp.json();
    }

    private handleError( error: Response | any ) {
        let errMsg: string;
        if ( error instanceof Response ) {
            const body = error.json() || '';
            const err = body.error || JSON.stringify( body );
            errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
        } else {
            errMsg = error.message ? error.message : error.toString();
        }
        console.error( errMsg );
    }
}