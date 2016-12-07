import { Component } from '@angular/core';
import { Response } from '@angular/http';

import { Job } from './job';
import { JobInfo } from './jobinfo';
import { JobInfoService } from './jobinfo.service';

@Component( {
    moduleId: module.id,
    selector: 'the-app',
    templateUrl: 'app.component.html'
})
export class AppComponent {
    name = 'JobInfo';
    title = "Job Info";

    needsAuthentication = false;
    notFound = false;

    username: string;
    password: string;

    jobId: string;
    system: string;
    lastJobId: string;
    lastSystem: string;

    jobInfo: JobInfo;

    constructor( private jobInfoSvc: JobInfoService ) {
    }

    submit(): void {
        let self = this;
        if ( this.jobId && this.system ) {
            this.needsAuthentication = false;
            this.notFound = false;
            this.lastJobId = this.jobId;
            this.lastSystem = this.system;
            this.jobInfoSvc.getJobInfo( this.username, this.password, this.jobId, this.system ).then( jobInfo => this.jobInfo = jobInfo ).catch( function( r ) {
                if ( r instanceof Response ) {
                    let response = r as Response;
                    let status = response.status;
                    if ( status === 401 ) {
                        self.needsAuthentication = true;
                    } else if ( status === 404 ) {
                        self.notFound = true;
                    }
                } else {
                    alert( "response is '" + r + "'" );
                }
            });
        }
    }

    handleError( response: Response | any ) {
        this.needsAuthentication = true;
        alert( "response is '" + response + "'" );
    }
}
