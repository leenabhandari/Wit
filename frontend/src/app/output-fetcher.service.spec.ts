import { TestBed } from '@angular/core/testing';

import { OutputFetcherService } from './output-fetcher.service';

describe('OutputFetcherService', () => {
  let service: OutputFetcherService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OutputFetcherService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
