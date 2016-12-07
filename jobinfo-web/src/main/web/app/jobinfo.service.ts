import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { JobInfo } from './jobinfo';
import { Job } from './job';

@Injectable()
export class JobInfoService {
    private baseURL = "http://localhost:4204/jobinfo-rest/jobinfo/rest";

    constructor( private http: Http ) {
    }

    getJobInfo( id: string, system: string ): Promise<JobInfo> {
        let url = this.baseURL + `?id=${id}&system=${system}`;
        return this.http.get( url ).toPromise().then( this.extract ).catch( this.handleError ) as Promise<JobInfo>;
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