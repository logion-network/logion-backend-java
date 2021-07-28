INSERT INTO transaction (block_number,
                         extrinsic_index,
                         created_on,
                         from_address,
                         to_address,
                         transfer_value,
                         tip,
                         fee,
                         reserved,
                         pallet,
                         method)
VALUES (1,
        1,
        '2021-07-19 15:17:11.000000',
        '5H4MvAsobfZ6bBCDyj5dsrWYLrA8HrRzaqa9p61UXtxMhSCY',
        '5CSbpCKSTvZefZYddesUQ9w6NDye2PHbf12MwBZGBgzGeGoo',
        13245000000,
        0,
        125000149,
        0,
        'balances',
        'transfer');

INSERT INTO transaction (block_number,
                         extrinsic_index,
                         created_on,
                         from_address,
                         to_address,
                         transfer_value,
                         tip,
                         fee,
                         reserved,
                         pallet,
                         method)
VALUES (2,
        1,
        '2021-07-20 15:17:11.000000',
        '5DPPdRwkgigKt2L7jxRfAoV4tfS89KgXsx47Wk3Kat5K6xPg',
        '5H4MvAsobfZ6bBCDyj5dsrWYLrA8HrRzaqa9p61UXtxMhSCY',
        0,
        0,
        125000141,
        23,
        'assets',
        'setMetadata');
